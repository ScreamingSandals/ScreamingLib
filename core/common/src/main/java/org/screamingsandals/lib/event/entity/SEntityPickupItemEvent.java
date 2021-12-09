package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SEntityPickupItemEvent extends SCancellableEvent {

    EntityBasic getEntity();

    EntityItem getItem();

    int getRemaining();
}
