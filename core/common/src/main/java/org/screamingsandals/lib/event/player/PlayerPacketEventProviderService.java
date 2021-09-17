package org.screamingsandals.lib.event.player;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.PacketMethod;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnPostEnable;

@Service(dependsOn = {
        EventManager.class
})
public class PlayerPacketEventProviderService {
    private static final String CHANNEL_NAME = "SPacketInboundOutboundChannelHandler";

    @OnPostEnable
    public void onPostEnable() {
        EventManager.getDefaultEventManager().register(SPlayerLoginEvent.class, sPlayerLoginEvent -> addPlayer(sPlayerLoginEvent.getPlayer(), true), EventPriority.LOWEST);
        EventManager.getDefaultEventManager().register(SPlayerJoinEvent.class, sPlayerJoinEvent -> addPlayer(sPlayerJoinEvent.getPlayer(), false), EventPriority.HIGH);
        EventManager.getDefaultEventManager().register(SPlayerLeaveEvent.class, sPlayerLeaveEvent -> removePlayer(sPlayerLeaveEvent.getPlayer()), EventPriority.HIGHEST);
        PlayerMapper.getPlayers().forEach(player -> addPlayer(player, false));
    }

    public void addPlayer(PlayerWrapper player, boolean onLogin) {
        try {
            final var channel = player.getChannel();
            if (channel == null) {
                throw new UnsupportedOperationException("Failed to find player channel!");
            }

            final var handler = new ChannelDuplexHandler() {
                @Override
                public void channelRead(ChannelHandlerContext ctx, Object packet) {
                    try {
                        final var event = EventManager.fire(new SPlayerPacketEvent(player, PacketMethod.INBOUND, packet));
                        if (!event.isCancelled()) {
                            packet = event.getPacket();
                            if (packet != null) {
                                super.channelRead(ctx, packet);
                            }
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }

                @Override
                public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) {
                    try {
                        final var event = EventManager.fire(new SPlayerPacketEvent(player, PacketMethod.OUTBOUND, packet));
                        if (!event.isCancelled()) {
                            packet = event.getPacket();
                            if (packet != null) {
                                super.write(ctx, packet, promise);
                            }
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            };

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
}
