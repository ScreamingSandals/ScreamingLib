package org.screamingsandals.lib.packet;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.player.SPlayerJoinEvent;
import org.screamingsandals.lib.event.player.SPlayerLeaveEvent;
import org.screamingsandals.lib.event.player.SPlayerLoginEvent;
import org.screamingsandals.lib.packet.event.SPacketEvent;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.PacketMethod;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.annotations.methods.OnPostEnable;
import org.screamingsandals.lib.utils.annotations.methods.OnPreDisable;

@Service(dependsOn = {
        EventManager.class
})
@ServiceDependencies(dependsOn = {
        EventManager.class,
        PlayerMapper.class,
})
public class ProtocolInjector {
    private static final String CHANNEL_NAME = "SPacketInboundOutboundChannelHandler";

    @OnPostEnable
    public void onPostEnable() {
        EventManager.getDefaultEventManager().register(SPlayerLoginEvent.class, sPlayerLoginEvent -> addPlayer(sPlayerLoginEvent.getPlayer(), true), EventPriority.LOWEST);
        EventManager.getDefaultEventManager().register(SPlayerJoinEvent.class, sPlayerJoinEvent -> addPlayer(sPlayerJoinEvent.getPlayer(), false), EventPriority.HIGH);
        EventManager.getDefaultEventManager().register(SPlayerLeaveEvent.class, sPlayerLeaveEvent -> removePlayer(sPlayerLeaveEvent.getPlayer()), EventPriority.HIGHEST);
        Server.getConnectedPlayers().forEach(player -> addPlayer(player, false));
    }

    @OnPreDisable
    public void onPreDisable() {
        Server.getConnectedPlayers().forEach(this::removePlayer);
    }

    public void addPlayer(PlayerWrapper player, boolean onLogin) {
        try {
            final var channel = player.getChannel();
            if (channel == null) {
                throw new UnsupportedOperationException("Failed to find player channel!");
            }

            final var handler = new PacketHandler(player);
            if (channel.pipeline().get(CHANNEL_NAME) == null && channel.pipeline().get("packet_handler") != null) {
                channel.pipeline().addBefore("packet_handler", CHANNEL_NAME, handler);
            }
        } catch (Throwable t) {
            if (onLogin) {
                // could be possible we did this a bit too early, SPlayerJoinEvent will take care of this now.
                return;
            }
            t.printStackTrace();
            player.kick(Component.text("Failed to inject!, please rejoin.."));
        }
    }

    public void removePlayer(PlayerWrapper player) {
        try {
            Channel ch = player.getChannel();
            if (ch != null && ch.pipeline().get(CHANNEL_NAME) != null) {
                ch.pipeline().remove(CHANNEL_NAME);
            }
        } catch (Throwable ignored) {
        }
    }

    @RequiredArgsConstructor(staticName = "of")
    private static class PacketHandler extends ChannelDuplexHandler {
        private final PlayerWrapper player;

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object packet) {
            final var future = EventManager.fireAsync(new SPacketEvent(player, PacketMethod.INBOUND, packet));

            future.thenAccept(event -> {
                if (event.isCancelled()) {
                    return;
                }
                var modifiedPacket = event.getPacket();
                try {
                    super.channelRead(ctx, modifiedPacket);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            });
        }

        @Override
        public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) {
            final var future = EventManager.fireAsync(new SPacketEvent(player, PacketMethod.OUTBOUND, packet));

            future.thenAccept(event -> {
                if (event.isCancelled()) {
                    return;
                }
                var modifiedPacket = event.getPacket();
                try {
                    super.write(ctx, modifiedPacket, promise);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }
}
