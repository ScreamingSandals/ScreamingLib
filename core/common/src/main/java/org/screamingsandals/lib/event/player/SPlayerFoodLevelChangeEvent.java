package org.screamingsandals.lib.event.player;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.item.Item;

public interface SPlayerFoodLevelChangeEvent extends SCancellableEvent, SPlayerEvent, PlatformEventWrapper {

    @Nullable
    Item getItem();

    int getFoodLevel();

    void setFoodLevel(int foodLevel);
}
