package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.world.LocationHolder;

public interface SPlayerMoveEvent extends SCancellableEvent, SPlayerEvent {

    LocationHolder getCurrentLocation();

    LocationHolder getNewLocation();

    void setNewLocation(LocationHolder newLocation);
}
