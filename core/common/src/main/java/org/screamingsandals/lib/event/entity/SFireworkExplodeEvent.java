package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityFirework;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SFireworkExplodeEvent extends SCancellableEvent, PlatformEventWrapper {
    EntityFirework getEntity();
}
