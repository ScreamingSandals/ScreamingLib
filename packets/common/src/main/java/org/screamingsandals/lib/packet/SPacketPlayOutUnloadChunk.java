package org.screamingsandals.lib.packet;

public interface SPacketPlayOutUnloadChunk extends SPacket {
    void setChunkX(int chunkX);

    void setChunkZ(int chunkZ);
}
