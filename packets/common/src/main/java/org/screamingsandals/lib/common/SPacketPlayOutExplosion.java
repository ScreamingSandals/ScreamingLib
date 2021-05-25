package org.screamingsandals.lib.common;

import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.List;

public interface SPacketPlayOutExplosion {
    void setX(double x);

    void setY(double y);

    void setZ(double z);

    void setStrength(float strength);

    void setVelocity(Vector3Df velocity);

    void setBlocks(List<LocationHolder> blockLocations);
}
