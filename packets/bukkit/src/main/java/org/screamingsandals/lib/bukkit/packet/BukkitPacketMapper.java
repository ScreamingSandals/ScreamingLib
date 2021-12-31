package org.screamingsandals.lib.bukkit.packet;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.screamingsandals.lib.bukkit.packet.listener.ServerboundInteractPacketListener;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.ArmorStandAccessor;
import org.screamingsandals.lib.packet.*;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.logger.LoggerWrapper;
import org.screamingsandals.lib.vanilla.packet.PacketIdMapping;

@Service(dependsOn = {
        ServerboundInteractPacketListener.class
})
@RequiredArgsConstructor
public class BukkitPacketMapper extends PacketMapper {
    private final LoggerWrapper logger;

    @Override
    public void sendPacket0(PlayerWrapper player, AbstractPacket packet) {
        if (packet == null) {
            throw new UnsupportedOperationException("Packet cannot be null!");
        }
        if (player == null) {
            throw new UnsupportedOperationException("Player cannot be null!");
        }
        if (!player.isOnline()) {
            logger.trace("Ignoring packet!, not sending packet to offline player.");
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
                Runnable task = () -> {
                    var future = channel.writeAndFlush(writer.getBuffer());
                    future.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                };
                if (channel.eventLoop().inEventLoop()) {
                    task.run();
                } else {
                    channel.eventLoop().execute(task);
                }
            }

            writer.getAppendedPackets().forEach(extraPacket -> sendPacket0(player, extraPacket));
        } catch(Throwable t) {
            Bukkit.getLogger().severe("An exception occurred sending packet of class: " + packet.getClass().getSimpleName() + " to player: " + player.getName());
            t.printStackTrace();
        }
    }

    @Override
    public int getId0(Class<? extends AbstractPacket> clazz) {
        return PacketIdMapping.getPacketId(clazz);
    }

    @Override
    public int getArmorStandTypeId0() {
        return ClassStorage.getEntityTypeId("armor_stand", ArmorStandAccessor.getType());
    }
}
