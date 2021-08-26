package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.world.LocationHolder;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundBlockEventPacket extends AbstractPacket {
    private LocationHolder location;
    private byte actionId;
    private byte actionParameter;
    private BlockTypeHolder blockData;

    @Override
    public void write(PacketWriter writer) {
        writer.writeBlockPosition(location);
        writer.writeByte(actionId);
        writer.writeByte(actionParameter);
        writer.writeBlockData(blockData);
    }
}
