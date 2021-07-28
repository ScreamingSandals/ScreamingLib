package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.screamingsandals.lib.world.LocationHolder;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundBlockDestructionPacket extends AbstractPacket {
    private int entityId;
    private LocationHolder blockLocation;
    private byte destroyStage;

    @Override
    public void write(PacketWriter writer) {
        writer.writeVarInt(entityId);
        writer.writeBlockPosition(blockLocation);
        writer.writeByte(destroyStage);
    }
}
