package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.world.LocationHolder;

public interface SItemDespawnEvent extends SCancellableEvent {
    EntityBasic getEntity();

    LocationHolder getLocation();
}
