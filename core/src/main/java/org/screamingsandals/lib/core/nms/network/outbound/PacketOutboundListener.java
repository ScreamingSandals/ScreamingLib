package org.screamingsandals.lib.core.nms.network.outbound;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.entity.Player;

import static org.screamingsandals.lib.core.reflect.SReflect.getField;
import static org.screamingsandals.lib.core.nms.utils.ClassStorage.getPlayerConnection;

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
			var manager = getField(getPlayerConnection(player), "networkManager,field_147371_a");
			return (Channel) getField(manager, "channel,field_150746_k,k,m");
		} catch (Throwable ignored) {
		}
		return null;
	}
	
	protected abstract Object handle(Player p, Object packet) throws Throwable;
}
