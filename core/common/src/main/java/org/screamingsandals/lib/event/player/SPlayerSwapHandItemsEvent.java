package org.screamingsandals.lib.event.player;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.item.Item;
public interface SPlayerSwapHandItemsEvent extends SCancellableEvent, SPlayerEvent, PlatformEventWrapper {

    @Nullable
    Item getMainHandItem();

    void setMainHandItem(@Nullable Item mainHandItem);

    @Nullable
    Item getOffHandItem();

    void setOffHandItem(@Nullable Item offHandItem);
}
