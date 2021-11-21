package org.screamingsandals.lib.event.player;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.item.Item;

public interface SPlayerItemConsumeEvent extends SCancellableEvent, SPlayerEvent {

    @Nullable
    Item getItem();

    void setItem(@Nullable Item item);
}
