package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SSheepRegrowWoolEvent extends SCancellableEvent {
    EntityBasic getEntity();
}
