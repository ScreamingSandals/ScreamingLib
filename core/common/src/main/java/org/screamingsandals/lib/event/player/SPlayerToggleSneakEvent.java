package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SPlayerToggleSneakEvent extends SCancellableEvent, SPlayerEvent, PlatformEventWrapper {

    boolean isSneaking();
}
