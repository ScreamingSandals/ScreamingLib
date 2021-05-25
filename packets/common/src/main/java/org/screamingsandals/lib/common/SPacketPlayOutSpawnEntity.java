package org.screamingsandals.lib.common;

import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.UUID;

public interface SPacketPlayOutSpawnEntity {
    void setEntityId(int entityId);

    void setUUID(UUID uuid);

    void setLocation(LocationHolder location);

    void setVelocity(Vector3D velocity);

    void setType(int typeId);

    void setObjectData(int objectData);

    void setDataWatcher(Object dataWatcher, int entityId);
}
