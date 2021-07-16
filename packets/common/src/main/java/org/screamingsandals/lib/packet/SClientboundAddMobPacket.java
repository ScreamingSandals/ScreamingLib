package org.screamingsandals.lib.packet;
import org.screamingsandals.lib.entity.DataWatcher;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.UUID;

public interface SClientboundAddMobPacket extends SPacket {

    SClientboundAddMobPacket setEntityId(int entityId);

    SClientboundAddMobPacket setUUID(UUID uuid);

    SClientboundAddMobPacket setLocation(LocationHolder location);

    SClientboundAddMobPacket setVelocity(Vector3D velocity);

    SClientboundAddMobPacket setType(int typeId);

    SClientboundAddMobPacket setDataWatcher(DataWatcher dataWatcher);

    SClientboundAddMobPacket setHeadYaw(byte yaw);
}
