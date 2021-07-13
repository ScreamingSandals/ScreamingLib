package org.screamingsandals.lib.packet;

import org.screamingsandals.lib.entity.DataWatcher;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.UUID;

public interface SPacketPlayOutSpawnEntityLiving extends SPacket {

    SPacketPlayOutSpawnEntityLiving setEntityId(int entityId);

    SPacketPlayOutSpawnEntityLiving setUUID(UUID uuid);

    SPacketPlayOutSpawnEntityLiving setType(int entityType);

    SPacketPlayOutSpawnEntityLiving setLocation(LocationHolder location);

    SPacketPlayOutSpawnEntityLiving setVelocity(Vector3D velocity);

    SPacketPlayOutSpawnEntityLiving setYaw(float yaw);

    SPacketPlayOutSpawnEntityLiving setPitch(float pitch);

    SPacketPlayOutSpawnEntityLiving setHeadYaw(float headPitch);

    SPacketPlayOutSpawnEntityLiving setDataWatcher(DataWatcher dataWatcher);
}
