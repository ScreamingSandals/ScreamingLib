package org.screamingsandals.lib.packet;

import org.screamingsandals.lib.world.LocationHolder;

public interface SPacketPlayOutEntityTeleport extends SPacket {

    SPacketPlayOutEntityTeleport setEntityId(int entityId);

    SPacketPlayOutEntityTeleport setLocation(LocationHolder location);

    SPacketPlayOutEntityTeleport setIsOnGround(boolean isOnGround);
}
