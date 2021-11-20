package org.screamingsandals.lib.event.world;

import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.world.WorldHolder;

public interface SWorldUnloadEvent extends SCancellableEvent {

    WorldHolder getWorld();
}
