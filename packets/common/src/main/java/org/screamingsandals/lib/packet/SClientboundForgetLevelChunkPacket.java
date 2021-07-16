package org.screamingsandals.lib.packet;

public interface SClientboundForgetLevelChunkPacket extends SPacket {

    SClientboundForgetLevelChunkPacket setChunkX(int chunkX);

    SClientboundForgetLevelChunkPacket setChunkZ(int chunkZ);
}
