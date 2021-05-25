package org.screamingsandals.lib.common;

import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.world.LocationHolder;

public interface SPacketPlayOutEntityTeleport {
    void setEntityId(int entityId);

    void setLocation(LocationHolder location);

    void setIsOnGround(boolean isOnGround);
}
