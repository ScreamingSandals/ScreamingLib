package org.screamingsandals.lib.common;

import org.screamingsandals.lib.utils.math.Vector3D;

public interface SPacketPlayOutEntityVelocity {
    void setEntityId(int entityId);

    void setVelocity(Vector3D velocity);
}
