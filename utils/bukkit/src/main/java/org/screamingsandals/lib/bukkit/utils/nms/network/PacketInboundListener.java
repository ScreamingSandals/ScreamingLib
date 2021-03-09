package org.screamingsandals.lib.bukkit.utils.nms.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.utils.reflect.Reflect;

import static org.screamingsandals.lib.bukkit.utils.nms.ClassStorage.*;

public abstract class PacketInboundListener{

	private static int ID = 0;

	private final String channelName = PacketInboundListener.class.getCanonicalName() + "-" + ID++;
	
	public void addPlayer(Player player) {
		try {
			final var channel = getChannel(player);
			if (channel == null) {
				return;
			}

			if (channel.pipeline().get(channelName) == null) {
				final var handler = new ChannelDuplexHandler() {
					@Override
					public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
						try {
							msg = handle(player, msg);
						} catch (Throwable t) {
							t.printStackTrace();
						}
						if (msg != null) {
							super.channelRead(ctx, msg);
						}
					}
				};
				channel.pipeline().addBefore("packet_handler", channelName, handler);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void removePlayer(Player player) {
		try {
			Channel ch = getChannel(player);
			if (ch.pipeline().get(channelName) != null) {
				ch.pipeline().remove(channelName);
			}
		} catch (Throwable t) {
		}
	}
	
	private Channel getChannel(Player player) {
		try {
			final var manager = Reflect.getField(getPlayerConnection(player), "networkManager,field_147371_a");
			return (Channel) Reflect.getField(manager, "channel,field_150746_k,k,m");
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}
	
	protected abstract Object handle(Player p, Object packet) throws Throwable;
}
