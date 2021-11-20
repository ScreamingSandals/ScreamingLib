package org.screamingsandals.lib.event.world;

import org.screamingsandals.lib.event.SEvent;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.WorldHolder;

public interface SSpawnChangeEvent extends SEvent {

    WorldHolder getWorld();

    LocationHolder getOldLocation();

    LocationHolder getNewLocation();
}
