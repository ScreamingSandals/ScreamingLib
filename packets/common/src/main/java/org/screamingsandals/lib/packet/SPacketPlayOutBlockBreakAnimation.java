package org.screamingsandals.lib.packet;

import org.screamingsandals.lib.world.LocationHolder;

public interface SPacketPlayOutBlockBreakAnimation extends SPacket {

    SPacketPlayOutBlockBreakAnimation setEntityId(int entityId);

    SPacketPlayOutBlockBreakAnimation setBlockLocation(LocationHolder blockLocation);

    SPacketPlayOutBlockBreakAnimation setDestroyStage(int destroyStage);
}
