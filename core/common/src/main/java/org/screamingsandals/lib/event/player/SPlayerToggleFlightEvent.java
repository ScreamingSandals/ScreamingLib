package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.SCancellableEvent;

public interface SPlayerToggleFlightEvent extends SCancellableEvent, SPlayerEvent {

    boolean isFlying();
}
