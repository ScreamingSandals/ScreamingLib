package org.screamingsandals.lib.packet;

import org.screamingsandals.lib.world.LocationHolder;

public interface SClientboundTeleportEntityPacket extends SPacket {

    SClientboundTeleportEntityPacket setEntityId(int entityId);

    SClientboundTeleportEntityPacket setLocation(LocationHolder location);

    SClientboundTeleportEntityPacket setIsOnGround(boolean isOnGround);
}
