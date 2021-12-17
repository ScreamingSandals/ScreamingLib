package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.world.LocationHolder;

public interface SPlayerMoveEvent extends SCancellableEvent, SPlayerEvent, PlatformEventWrapper {

    LocationHolder getCurrentLocation();

    LocationHolder getNewLocation();

    void setNewLocation(LocationHolder newLocation);
}
