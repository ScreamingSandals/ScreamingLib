package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SSlimeSplitEvent extends SCancellableEvent {

    EntityBasic getEntity();

    int getCount();

    void setCount(int count);
}
