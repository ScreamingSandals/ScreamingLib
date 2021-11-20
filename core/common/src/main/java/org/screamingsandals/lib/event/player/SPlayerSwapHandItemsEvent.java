package org.screamingsandals.lib.event.player;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.item.Item;
public interface SPlayerSwapHandItemsEvent extends SCancellableEvent, SPlayerEvent {

    @Nullable
    Item getMainHandItem();

    void setMainHandItem(@Nullable Item mainHandItem);

    @Nullable
    Item getOffHandItem();

    void setOffHandItem(@Nullable Item offHandItem);
}
