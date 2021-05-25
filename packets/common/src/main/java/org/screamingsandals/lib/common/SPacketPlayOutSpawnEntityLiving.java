package org.screamingsandals.lib.common;

import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.UUID;

public interface SPacketPlayOutSpawnEntityLiving {
    void setEntityId(int entityId);

    void setUUID(UUID uuid);

    void setType(int entityType);

    void setLocation(LocationHolder location);

    void setVelocity(Vector3D velocity);

    void setYaw(float yaw);

    void setPitch(float pitch);

    void setHeadPitch(float headPitch);

    void setDataWatcher(Object dataWatcher, int entityId);
}
