package org.screamingsandals.lib.packet;

import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.UUID;

public interface SClientboundAddEntityPacket extends SPacket {

    SClientboundAddEntityPacket setEntityId(int id);

    SClientboundAddEntityPacket setUUID(UUID uuid);

    SClientboundAddEntityPacket setLocation(LocationHolder location);

    SClientboundAddEntityPacket setVelocity(Vector3D velocity);

    SClientboundAddEntityPacket setType(int typeId);

    SClientboundAddEntityPacket setObjectData(int data);
}
