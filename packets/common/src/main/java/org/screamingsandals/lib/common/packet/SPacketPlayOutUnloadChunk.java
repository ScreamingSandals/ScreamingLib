package org.screamingsandals.lib.common.packet;

public interface SPacketPlayOutUnloadChunk extends SPacket {
    void setChunkX(int chunkX);

    void setChunkZ(int chunkZ);
}
