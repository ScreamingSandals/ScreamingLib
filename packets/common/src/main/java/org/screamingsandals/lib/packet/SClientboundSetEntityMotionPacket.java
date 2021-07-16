package org.screamingsandals.lib.packet;

import org.screamingsandals.lib.utils.math.Vector3D;

public interface SClientboundSetEntityMotionPacket extends SPacket {
    SClientboundSetEntityMotionPacket setEntityId(int entityId);

    SClientboundSetEntityMotionPacket setVelocity(Vector3D velocity);
}
