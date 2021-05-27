package org.screamingsandals.lib.packet;

import org.screamingsandals.lib.utils.math.Vector3D;

public interface SPacketPlayOutEntityVelocity extends SPacket {
    void setEntityId(int entityId);

    void setVelocity(Vector3D velocity);
}
