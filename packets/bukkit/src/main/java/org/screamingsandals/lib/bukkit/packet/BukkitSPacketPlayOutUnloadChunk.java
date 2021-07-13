package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutUnloadChunk;

public class BukkitSPacketPlayOutUnloadChunk extends BukkitSPacket implements SPacketPlayOutUnloadChunk {

    public BukkitSPacketPlayOutUnloadChunk() {
        super(ClassStorage.NMS.PacketPlayOutUnloadChunk);
    }

    @Override
    public SPacketPlayOutUnloadChunk setChunkX(int chunkX) {
        packet.setField("a,field_186942_a,f_132137_", chunkX);
        return this;
    }

    @Override
    public SPacketPlayOutUnloadChunk setChunkZ(int chunkZ) {
        packet.setField("b,field_186943_b,f_132138_", chunkZ);
        return this;
    }
}
