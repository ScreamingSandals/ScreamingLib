package org.screamingsandals.lib.packet;

import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.LocationHolder;

public interface SPacketPlayOutBlockAction extends SPacket {

    SPacketPlayOutBlockAction setBlockLocation(LocationHolder location);

    SPacketPlayOutBlockAction setActionId(int actionId);

    SPacketPlayOutBlockAction setActionParameter(int actionParameter);

    SPacketPlayOutBlockAction setBlockType(BlockHolder block);
}
