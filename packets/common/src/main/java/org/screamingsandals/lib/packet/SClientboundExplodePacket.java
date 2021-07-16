package org.screamingsandals.lib.packet;

import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.List;

public interface SClientboundExplodePacket extends SPacket {

    SClientboundExplodePacket setX(double x);

    SClientboundExplodePacket setY(double y);

    SClientboundExplodePacket setZ(double z);

    SClientboundExplodePacket setStrength(float strength);

    SClientboundExplodePacket setKnockBackVelocity(Vector3Df velocity);

    SClientboundExplodePacket setBlocks(List<LocationHolder> blockLocations);
}
