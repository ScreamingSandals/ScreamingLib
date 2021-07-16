package org.screamingsandals.lib.packet;

import org.screamingsandals.lib.world.BlockDataHolder;
import org.screamingsandals.lib.world.LocationHolder;

public interface SClientboundBlockUpdatePacket extends SPacket {

    SClientboundBlockUpdatePacket setBlockLocation(LocationHolder blockLocation);

    SClientboundBlockUpdatePacket setBlockData(BlockDataHolder blockData);
}
