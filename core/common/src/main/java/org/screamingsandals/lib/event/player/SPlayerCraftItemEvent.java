package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ClickType;
import org.screamingsandals.lib.utils.InventoryAction;
import org.screamingsandals.lib.utils.SlotType;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class SPlayerCraftItemEvent extends CancellableAbstractEvent {
    private final PlayerWrapper player;
    private Item currentItem;
    @Nullable private final Container clickedInventoryContainer;
    private final Container craftInventory;
    private final ClickType clickType;
    private final Recipe recipe;
    private Result result;
    private InventoryAction inventoryAction;
    private final Item cursor;
    private SlotType slotType;
    private final int hotbarButton;
    private final int rawSlot;

    @AllArgsConstructor
    @Data
    public static class Recipe {
        private final Item result;
    }
}
