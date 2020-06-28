package org.screamingsandals.lib.nms.network.inbound;

import org.bukkit.entity.Player;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

import static org.screamingsandals.lib.reflection.Reflection.*;
import static org.screamingsandals.lib.nms.utils.ClassStorage.*;

public abstract class PacketInboundListener{
	private static int ID = 0;

	private final String channelName = PacketInboundListener.class.getCanonicalName() + "-" + ID++;
	
	public void addPlayer(Player player) {
		try {
			var channel = getChannel(player);
			if (channel != null && channel.pipeline().get(channelName) == null) {
				var handler = new ChannelDuplexHandler() {
					@Override
					public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
						try {
							msg = handle(player, msg);
						} catch (Throwable ignored) {
						}
						if (msg != null) {
							super.channelRead(ctx, msg);
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
			var manager = getField(getPlayerConnection(player), "networkManager,field_147371_a");
			return (Channel) getField(manager, "channel,field_150746_k,k,m");
		} catch (Throwable ignored) {
		}
		return null;
	}
	
	protected abstract Object handle(Player p, Object packet) throws Throwable;
}
