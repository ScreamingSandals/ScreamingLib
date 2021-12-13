package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SArrowBodyCountChangeEvent extends SCancellableEvent {

    EntityBasic getEntity();

    boolean isReset();

    int getOldAmount();

    int getNewAmount();

    void setNewAmount(int newAmount);
}
