package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.SCancellableEvent;

public interface SPlayerToggleSneakEvent extends SCancellableEvent, SPlayerEvent {

    boolean isSneaking();
}
