package org.screamingsandals.lib.packet;

import io.netty.channel.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.event.player.SPlayerLeaveEvent;
import org.screamingsandals.lib.event.player.SPlayerLoginEvent;
import org.screamingsandals.lib.packet.event.SPacketEvent;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.PacketMethod;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.annotations.methods.OnDisable;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

// This class has been taken and slightly modified from ProtocolLib's TinyProtocol class
// see: https://github.com/dmulloy2/ProtocolLib/blob/master/TinyProtocol/src/main/java/com/comphenix/tinyprotocol/TinyProtocol.java

@Service(dependsOn = {
        EventManager.class
})
@ServiceDependencies(dependsOn = {
        EventManager.class,
        PlayerMapper.class,
})
public class ProtocolInjector {
    private static final String CHANNEL_NAME = "SPacketInboundOutboundChannelHandler";
    private final List<Channel> serverChannels;
    private final Object synchronizationObject;
    private final List<Channel> uninjectedChannels;
    private volatile boolean closed;
    private ChannelInboundHandlerAdapter serverChannelHandler;
    private ChannelInitializer<Channel> beginInitProtocol;
    private ChannelInitializer<Channel> endInitProtocol;

    public ProtocolInjector() {
        this.serverChannels = new ArrayList<>();
        this.uninjectedChannels = new ArrayList<>();
        synchronizationObject = Server.getNetworkManagerSynchronizationObject();
    }

    @OnEnable
    public void onEnable() {
        registerChannelHandler();
        Server.getConnectedPlayers().forEach(this::injectPlayer);
    }

    @OnDisable
    public void onDisable() {
        close();
    }

    private void createServerChannelHandler() {
        // Handle connected channels
        endInitProtocol = new ChannelInitializer<>() {
            @Override
            protected void initChannel(Channel channel) {
                try {
                    // This can take a while, so we need to stop the main thread from interfering
                    synchronized (synchronizationObject) {
                        // Stop injecting channels
                        if (!closed) {
                            channel.eventLoop().submit(() -> injectChannelInternal(channel));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };

        // This is executed before Minecraft's channel handler
        beginInitProtocol = new ChannelInitializer<>() {
            @Override
            protected void initChannel(Channel channel) {
                channel.pipeline().addLast(endInitProtocol);
            }
        };

        serverChannelHandler = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                final var channel = (Channel) msg;
                channel.pipeline().addFirst(beginInitProtocol);
                ctx.fireChannelRead(msg);
            }
        };
    }

    @SuppressWarnings("unchecked")
    private void registerChannelHandler() {
        createServerChannelHandler();
        final var channelFutures = Server.getConnections();
        for (var future : channelFutures) {
            Channel serverChannel = future.channel();
            serverChannels.add(serverChannel);
            serverChannel.pipeline().addFirst(serverChannelHandler);
        }
    }

    private void unregisterChannelHandler() {
        if (serverChannelHandler == null)
            return;

        for (Channel serverChannel : serverChannels) {
            final ChannelPipeline pipeline = serverChannel.pipeline();

            // Remove channel handler
            serverChannel.eventLoop().execute(() -> {
                try {
                    pipeline.remove(serverChannelHandler);
                } catch (NoSuchElementException e) {
                    // That's fine
                }
            });
        }
    }

    @OnEvent(priority = EventPriority.LOWEST)
    public void onPlayerLogin(SPlayerLoginEvent event) {
        if (closed) {
            return;
        }

        final var player = event.getPlayer();
        final var channel = player.getChannel();
        // Don't inject players that have been explicitly uninjected
        if (!uninjectedChannels.contains(channel)) {
            injectPlayer(player);
        }
    }

    @OnEvent(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(SPlayerLeaveEvent event) {
        uninjectedChannels.remove(event.getPlayer().getChannel());
    }

    public void injectPlayer(PlayerWrapper player) {
        injectChannelInternal(player.getChannel()).setPlayer(player);
    }

    private PacketHandler injectChannelInternal(Channel channel) {
        try {
            PacketHandler handler = (PacketHandler) channel.pipeline().get(CHANNEL_NAME);

            // Inject our packet interceptor
            if (handler == null) {
                handler = new PacketHandler();
                channel.pipeline().addBefore("packet_handler", CHANNEL_NAME, handler);
                uninjectedChannels.remove(channel);
            }

            return handler;
        } catch (Throwable t) {
            // Try again
            return (PacketHandler) channel.pipeline().get(CHANNEL_NAME);
        }
    }

    public void uninjectPlayer(PlayerWrapper player) {
        uninjectChannel(player.getChannel());
    }

    public void uninjectChannel(Channel channel) {
        // No need to guard against this if we're closing
        if (!closed) {
            uninjectedChannels.add(channel);
        }

        // See ChannelInjector in ProtocolLib, line 590
        channel.eventLoop().execute(() -> channel.pipeline().remove(CHANNEL_NAME));
    }


    public void close() {
        if (!closed) {
            closed = true;
            Server.getConnectedPlayers().forEach(this::uninjectPlayer);
            unregisterChannelHandler();
        }
    }

    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @Data
    private static class PacketHandler extends ChannelDuplexHandler {
        private volatile PlayerWrapper player;

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object packet) {
            final var future = EventManager.fireAsync(new SPacketEvent(player, PacketMethod.INBOUND, packet, ctx.channel()));

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
            final var future = EventManager.fireAsync(new SPacketEvent(player, PacketMethod.OUTBOUND, packet, ctx.channel()));

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
