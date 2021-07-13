package org.screamingsandals.lib.packet;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.UUID;

public interface SPacketPlayOutSpawnEntity extends SPacket {

    SPacketPlayOutSpawnEntity setEntityId(int entityId);

    SPacketPlayOutSpawnEntity setUUID(UUID uuid);

    SPacketPlayOutSpawnEntity setLocation(LocationHolder location);

    SPacketPlayOutSpawnEntity setVelocity(Vector3D velocity);

    SPacketPlayOutSpawnEntity setType(int typeId);

    SPacketPlayOutSpawnEntity setObjectData(int objectData);
}
