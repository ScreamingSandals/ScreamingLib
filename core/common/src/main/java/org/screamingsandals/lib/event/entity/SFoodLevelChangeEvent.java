package org.screamingsandals.lib.event.entity;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.item.Item;

public interface SFoodLevelChangeEvent extends SCancellableEvent {

    EntityBasic getEntity();

    int getLevel();

    void setLevel(int level);

    @Nullable
    Item getItem();
}
