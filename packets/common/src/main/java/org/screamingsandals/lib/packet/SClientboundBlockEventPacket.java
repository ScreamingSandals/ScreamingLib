package org.screamingsandals.lib.packet;

import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.LocationHolder;

public interface SClientboundBlockEventPacket extends SPacket {

    SClientboundBlockEventPacket setBlockLocation(LocationHolder location);

    SClientboundBlockEventPacket setActionId(int actionId);

    SClientboundBlockEventPacket setActionParameter(int actionParameter);

    SClientboundBlockEventPacket setBlockType(BlockHolder block);
}
