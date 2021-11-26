package org.screamingsandals.lib.event.player;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.utils.*;

public interface SPlayerInventoryClickEvent extends SCancellableEvent, SPlayerEvent {

    @Nullable
    Item getCursorItem();

    @Nullable
    Item getCurrentItem();

    void setCurrentItem(Item currentItem);

    @Nullable
    Container getClickedInventory();

    // Who tf called this method getContainer()?
    @Nullable
    @Deprecated
    default Container getContainer() {
        return getClickedInventory();
    }

    ClickType getClickType();

    Container getInventory();

    InventoryAction getAction();

    int getHotbarButton();

    int getSlot();

    SlotType getSlotType();

    int getRawSlot();

    Result getResult();

    void setResult(Result result);
}
