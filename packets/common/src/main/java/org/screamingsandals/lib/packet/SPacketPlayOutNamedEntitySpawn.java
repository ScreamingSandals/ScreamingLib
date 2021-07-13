package org.screamingsandals.lib.packet;
import org.screamingsandals.lib.entity.DataWatcher;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.List;
import java.util.UUID;

public interface SPacketPlayOutNamedEntitySpawn extends SPacket {

    SPacketPlayOutNamedEntitySpawn setEntityId(int entityId);

    SPacketPlayOutNamedEntitySpawn setUUID(UUID uuid);

    SPacketPlayOutNamedEntitySpawn setLocation(LocationHolder location);

    SPacketPlayOutNamedEntitySpawn setYaw(float yaw);

    SPacketPlayOutNamedEntitySpawn setPitch(float pitch);

    SPacketPlayOutNamedEntitySpawn setDataWatcher(DataWatcher dataWatcher);

    SPacketPlayOutNamedEntitySpawn setItems(List<DataWatcher.Item<?>> items);
}
