package org.screamingsandals.lib.event.player;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.utils.*;

public interface SPlayerCraftItemEvent extends SPlayerInventoryClickEvent {

    // Who tf called this method getClickedInventoryContainer()?
    @Nullable
    @Deprecated
    default Container getClickedInventoryContainer() {
        return getClickedInventory();
    }

    // That's the same thing like getInventory()
    @Deprecated
    default Container getCraftInventory() {
        return getInventory();
    }

    Recipe getRecipe();

    // TODO: we should create proper Recipe API
    interface Recipe extends Wrapper, RawValueHolder {

        Item getResult();
    }
}
