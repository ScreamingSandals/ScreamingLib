package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SEntityAirChangeEvent extends SCancellableEvent, PlatformEventWrapper {

    EntityBasic getEntity();

    int getAmount();

    void setAmount(int amount);
}
