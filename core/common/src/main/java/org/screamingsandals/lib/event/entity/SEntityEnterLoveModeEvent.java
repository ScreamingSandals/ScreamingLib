package org.screamingsandals.lib.event.entity;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.SCancellableEvent;


public interface SEntityEnterLoveModeEvent extends SCancellableEvent {
    EntityBasic getEntity();

    @Nullable
    EntityBasic getHumanEntity();

    int getTicksInLove();

    void setTicksInLove(int ticksInLove);
}
