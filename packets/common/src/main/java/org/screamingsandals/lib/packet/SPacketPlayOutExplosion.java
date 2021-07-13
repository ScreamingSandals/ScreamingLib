package org.screamingsandals.lib.packet;

import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.List;

public interface SPacketPlayOutExplosion extends SPacket {

    SPacketPlayOutExplosion setX(double x);

    SPacketPlayOutExplosion setY(double y);

    SPacketPlayOutExplosion setZ(double z);

    SPacketPlayOutExplosion setStrength(float strength);

    SPacketPlayOutExplosion setKnockBackVelocity(Vector3Df velocity);

    SPacketPlayOutExplosion setBlocks(List<LocationHolder> blockLocations);
}
