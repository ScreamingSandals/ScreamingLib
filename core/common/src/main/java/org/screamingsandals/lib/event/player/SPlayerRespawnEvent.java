package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.world.LocationHolder;

public interface SPlayerRespawnEvent extends SCancellableEvent, SPlayerEvent {

    LocationHolder getLocation();

    void setLocation(LocationHolder location);
}
