package org.screamingsandals.lib.packet;
import org.screamingsandals.lib.entity.DataWatcher;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.UUID;

public interface SPacketPlayOutNamedEntitySpawn extends SPacket {
    void setEntityId(int entityId);

    void setUUID(UUID uuid);

    void setLocation(LocationHolder location);

    void setYaw(float yaw);

    void setPitch(float pitch);

    void setDataWatcher(DataWatcher dataWatcher);
}
