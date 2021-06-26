package org.screamingsandals.lib.bukkit.utils.nms.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.utils.reflect.Reflect;

import static org.screamingsandals.lib.bukkit.utils.nms.ClassStorage.*;

public abstract class PacketOutboundListener {
    private static int ID = 0;

    private final String channelName = PacketOutboundListener.class.getCanonicalName() + "-" + ID++;

    public void addPlayer(Player player) {
        try {
            var channel = getChannel(player);
            if (channel != null && channel.pipeline().get(channelName) == null) {
                var handler = new ChannelDuplexHandler() {
                    @Override
                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                        try {
                            msg = handle(player, msg);
                        } catch (Throwable ignored) {
                        }

                        if (msg != null) {
                            super.write(ctx, msg, promise);
                        }
                    }
                };
                channel.pipeline().addBefore("packet_handler", channelName, handler);
            }
        } catch (Throwable ignored) {
        }
    }

    public void removePlayer(Player player) {
        try {
            var channel = getChannel(player);
            if (channel != null && channel.pipeline().get(channelName) != null) {
                channel.pipeline().remove(channelName);
            }
        } catch (Throwable ignored) {
        }
    }

    private Channel getChannel(Player player) {
        try {
            var manager = Reflect.getField(getPlayerConnection(player), "connection,networkManager,field_147371_a");
            return (Channel) Reflect.getField(manager, "channel,field_150746_k,k,m");
        } catch (Throwable ignored) {
        }
        return null;
    }

    protected abstract Object handle(Player p, Object packet) throws Throwable;
}
