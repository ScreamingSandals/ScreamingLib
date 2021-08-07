package org.screamingsandals.lib.bukkit.packet;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.ConnectionAccessor;
import org.screamingsandals.lib.nms.accessors.ServerGamePacketListenerImplAccessor;
import org.screamingsandals.lib.nms.accessors.ServerPlayerAccessor;
import org.screamingsandals.lib.packet.*;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.vanilla.packet.PacketIdMapping;

@Service
@Slf4j
public class BukkitPacketMapper extends PacketMapper {
    private static final ChannelFutureListener OPERATION_COMPLETE_LISTENER = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if (!future.isSuccess()) {
                future.cause().printStackTrace();
                future.channel().pipeline().fireExceptionCaught(future.cause());
            }
        }
    };

    public static void init() {
        PacketMapper.init(BukkitPacketMapper::new);
    }

    @Override
    public void sendPacket0(PlayerWrapper player, AbstractPacket packet) {
        if (packet == null) {
            throw new UnsupportedOperationException("Packet cannot be null!");
        }
        if (player == null) {
            throw new UnsupportedOperationException("Player cannot be null!");
        }
        if (!player.isOnline()) {
            log.trace("Ignoring packet!, not sending packet to offline player.");
            return;
        }

        try {
            var writer = new CraftBukkitPacketWriter(Unpooled.buffer());
            writer.writeVarInt(PacketIdMapping.getPacketId(packet.getClass()));

            int i = writer.getBuffer().writerIndex();
            packet.write(writer);

            int j = writer.getBuffer().writerIndex() - i;
            if (j > 2097152) {
                throw new IllegalArgumentException("Packet too big (is " + j + ", should be less than 2097152): " + packet);
            }

            var channel = Reflect.getFieldResulted(ClassStorage.getHandle(player.as(Player.class)), ServerPlayerAccessor.getFieldConnection())
                    .getFieldResulted(ServerGamePacketListenerImplAccessor.getFieldConnection())
                    .getFieldResulted(ConnectionAccessor.getFieldChannel())
                    .as(Channel.class);

            if (channel.eventLoop().inEventLoop()) {
                var future = channel.writeAndFlush(writer.getBuffer());
                future.addListener(OPERATION_COMPLETE_LISTENER);
            } else {
                channel.eventLoop().execute(() -> {
                    var future = channel.writeAndFlush(writer.getBuffer());
                    future.addListener(OPERATION_COMPLETE_LISTENER);
                });
            }

            writer.getAppendedPackets().forEach(packet1 -> sendPacket0(player, packet1));
        } catch(Throwable t) {
            Bukkit.getLogger().severe("An exception occurred sending packet of class: " + packet.getClass().getSimpleName() + " to player: " + player.getName());
            t.printStackTrace();
        }
    }

}
