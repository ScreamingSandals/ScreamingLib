package org.screamingsandals.lib.packet;

import org.screamingsandals.lib.utils.math.Vector3D;

public interface SPacketPlayOutEntityVelocity extends SPacket {
    SPacketPlayOutEntityVelocity setEntityId(int entityId);

    SPacketPlayOutEntityVelocity setVelocity(Vector3D velocity);
}
