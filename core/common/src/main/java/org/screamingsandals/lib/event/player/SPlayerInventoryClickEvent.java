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
import org.screamingsandals.lib.utils.InventoryType;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class SPlayerInventoryClickEvent extends CancellableAbstractEvent {
    private final PlayerWrapper player;
    @Nullable
    private final Item cursorItem;
    @Nullable
    private final Item currentItem;
    @Nullable
    private final Container container;
    private final ClickType clickType;
    private final Container inventory;
    private final InventoryAction action;
    private final int hotbarButton;
    private final int slot;
    private final InventoryType.SlotType slotType;
    private final int rawSlot;
    private Result result;
}
