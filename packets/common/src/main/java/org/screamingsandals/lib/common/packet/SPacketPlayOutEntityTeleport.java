package org.screamingsandals.lib.common.packet;

import org.screamingsandals.lib.world.LocationHolder;

public interface SPacketPlayOutEntityTeleport extends SPacket {
    void setEntityId(int entityId);

    void setLocation(LocationHolder location);

    void setIsOnGround(boolean isOnGround);
}
