package org.screamingsandals.lib.common;

import org.screamingsandals.lib.world.BlockDataHolder;
import org.screamingsandals.lib.world.LocationHolder;

public interface SPacketPlayOutBlockChange extends SPacket {
    void setBlockLocation(LocationHolder blockLocation);

    void setBlockData(BlockDataHolder blockData);
}
