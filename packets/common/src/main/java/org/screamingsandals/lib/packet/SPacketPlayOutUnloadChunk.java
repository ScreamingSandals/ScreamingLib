package org.screamingsandals.lib.packet;

public interface SPacketPlayOutUnloadChunk extends SPacket {

    SPacketPlayOutUnloadChunk setChunkX(int chunkX);

    SPacketPlayOutUnloadChunk setChunkZ(int chunkZ);
}
