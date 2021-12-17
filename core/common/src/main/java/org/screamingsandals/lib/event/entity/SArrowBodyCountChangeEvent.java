package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SArrowBodyCountChangeEvent extends SCancellableEvent, PlatformEventWrapper {

    EntityBasic getEntity();

    boolean isReset();

    int getOldAmount();

    int getNewAmount();

    void setNewAmount(int newAmount);
}
