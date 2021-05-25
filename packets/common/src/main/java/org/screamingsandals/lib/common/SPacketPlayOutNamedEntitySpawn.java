package org.screamingsandals.lib.common;
import org.screamingsandals.lib.utils.math.Vector3D;

import java.util.UUID;

public interface SPacketPlayOutNamedEntitySpawn {
    void setEntityId(int entityId);

    void setUUID(UUID uuid);

    void setLocation(Vector3D location);

    void setYaw(float yaw);

    void setPitch(float pitch);

    void setDataWatcher(Object dataWatcher, int entityId);
}
