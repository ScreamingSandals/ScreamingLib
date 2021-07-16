package org.screamingsandals.lib.bukkit.packet;
import org.screamingsandals.lib.nms.accessors.ClientboundForgetLevelChunkPacketAccessor;
import org.screamingsandals.lib.packet.SClientboundForgetLevelChunkPacket;

public class BukkitSClientboundForgetLevelChunkPacket extends BukkitSPacket implements SClientboundForgetLevelChunkPacket {

    public BukkitSClientboundForgetLevelChunkPacket() {
        super(ClientboundForgetLevelChunkPacketAccessor.getType());
    }

    @Override
    public SClientboundForgetLevelChunkPacket setChunkX(int chunkX) {
        packet.setField(ClientboundForgetLevelChunkPacketAccessor.getFieldX(), chunkX);
        return this;
    }

    @Override
    public SClientboundForgetLevelChunkPacket setChunkZ(int chunkZ) {
        packet.setField(ClientboundForgetLevelChunkPacketAccessor.getFieldZ(), chunkZ);
        return this;
    }
}
