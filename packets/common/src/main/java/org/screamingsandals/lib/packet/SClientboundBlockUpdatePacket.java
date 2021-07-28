package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.screamingsandals.lib.world.BlockDataHolder;
import org.screamingsandals.lib.world.LocationHolder;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundBlockUpdatePacket extends AbstractPacket {
    private LocationHolder blockLocation;
    private BlockDataHolder blockData;

    @Override
    public void write(PacketWriter writer) {
        writer.writeBlockPosition(blockLocation);
        writer.writeBlockData(blockData);
    }
}
