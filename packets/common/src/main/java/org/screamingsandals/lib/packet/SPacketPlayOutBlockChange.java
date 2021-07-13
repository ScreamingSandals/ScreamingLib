package org.screamingsandals.lib.packet;

import org.screamingsandals.lib.world.BlockDataHolder;
import org.screamingsandals.lib.world.LocationHolder;

public interface SPacketPlayOutBlockChange extends SPacket {

    SPacketPlayOutBlockChange setBlockLocation(LocationHolder blockLocation);

    SPacketPlayOutBlockChange setBlockData(BlockDataHolder blockData);
}
