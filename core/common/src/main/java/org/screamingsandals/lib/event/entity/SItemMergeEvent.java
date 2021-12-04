package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SItemMergeEvent extends SCancellableEvent {

    EntityItem getEntity();

    EntityItem getTarget();
}
