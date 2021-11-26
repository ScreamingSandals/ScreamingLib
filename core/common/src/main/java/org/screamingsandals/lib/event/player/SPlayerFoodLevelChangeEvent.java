package org.screamingsandals.lib.event.player;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.item.Item;

public interface SPlayerFoodLevelChangeEvent extends SCancellableEvent, SPlayerEvent {

    @Nullable
    Item getItem();

    int getFoodLevel();

    void setFoodLevel(int foodLevel);
}
