package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundForgetLevelChunkPacket extends AbstractPacket {
    private int chunkX;
    private int chunkZ;

    @Override
    public void write(PacketWriter writer) {
        writer.writeInt(chunkX);
        writer.writeInt(chunkZ);
    }
}
