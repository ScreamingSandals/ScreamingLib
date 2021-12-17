package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SPlayerToggleSprintEvent extends SCancellableEvent, SPlayerEvent, PlatformEventWrapper {

    boolean isSprinting();
}
