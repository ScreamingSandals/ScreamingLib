package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SEntityCombustEvent extends SCancellableEvent, PlatformEventWrapper {

    EntityBasic getEntity();

    int getDuration();

    void setDuration(int duration);
}
