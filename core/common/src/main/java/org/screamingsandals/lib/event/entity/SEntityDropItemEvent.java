package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SEntityDropItemEvent extends SCancellableEvent, PlatformEventWrapper {

    EntityBasic getEntity();

    EntityItem getDrop();
}
