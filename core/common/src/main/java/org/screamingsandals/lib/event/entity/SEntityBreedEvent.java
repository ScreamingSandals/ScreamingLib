package org.screamingsandals.lib.event.entity;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.item.Item;

public interface SEntityBreedEvent extends SCancellableEvent, PlatformEventWrapper {

    EntityBasic getEntity();

    EntityBasic getMother();

    EntityBasic getFather();

    @Nullable
    EntityBasic getBreeder();

    @Nullable
    Item getBredWith();

    int getExperience();

    void setExperience(int experience);
}
