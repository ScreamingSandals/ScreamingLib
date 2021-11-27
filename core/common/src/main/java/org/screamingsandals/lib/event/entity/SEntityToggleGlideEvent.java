package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.SCancellableEvent;
public interface SEntityToggleGlideEvent extends SCancellableEvent {

    EntityBasic getEntity();

    boolean isGliding();
}
