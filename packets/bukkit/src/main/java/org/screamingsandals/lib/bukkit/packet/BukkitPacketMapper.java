package org.screamingsandals.lib.bukkit.packet;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.screamingsandals.lib.packet.*;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.vanilla.packet.PacketIdMapping;

@Service
@Slf4j
public class BukkitPacketMapper extends PacketMapper {

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
            writer.writeVarInt(packet.getId());

            int i = writer.getBuffer().writerIndex();
            packet.write(writer);

            int j = writer.getBuffer().writerIndex() - i;
            if (j > 2097152) {
                throw new IllegalArgumentException("Packet too big (is " + j + ", should be less than 2097152): " + packet);
            }

            var channel = player.getChannel();
            if (channel.isActive()) {
                if (channel.eventLoop().inEventLoop()) {
                    var future = channel.writeAndFlush(writer.getBuffer());
                    future.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                } else {
                    channel.eventLoop().execute(() -> {
                        var future = channel.writeAndFlush(writer.getBuffer());
                        future.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                    });
                }
            }

            for (var extraPacket : writer.getAppendedPackets()) {
                sendPacket0(player, extraPacket);
            }
        } catch(Throwable t) {
            Bukkit.getLogger().severe("An exception occurred sending packet of class: " + packet.getClass().getSimpleName() + " to player: " + player.getName());
            t.printStackTrace();
        }
    }

    @Override
    public int getId0(Class<? extends AbstractPacket> clazz) {
        return PacketIdMapping.getPacketId(clazz);
    }

}
