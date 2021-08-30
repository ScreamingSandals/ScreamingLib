package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.*;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class SPlayerInventoryClickEvent extends CancellableAbstractEvent implements SPlayerEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ObjectLink<@Nullable Item> cursorItem; // this field is mutable in bukkit api
    private final ObjectLink<@Nullable Item> currentItem; // this field is mutable in bukkit api
    private final ImmutableObjectLink<@Nullable Container> container;
    private final ImmutableObjectLink<ClickType> clickType;
    private final ImmutableObjectLink<Container> inventory;
    private final ImmutableObjectLink<InventoryAction> action;
    private final ImmutableObjectLink<Integer> hotbarButton;
    private final ImmutableObjectLink<Integer> slot;
    private final ImmutableObjectLink<SlotType> slotType;
    private final ImmutableObjectLink<Integer> rawSlot;
    private final ObjectLink<Result> result;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    @Nullable
    public Item getCursorItem() {
        return cursorItem.get();
    }

    @Nullable
    public Item getCurrentItem() {
        return currentItem.get();
    }

    @Nullable
    public Container getContainer() {
        return container.get();
    }

    public ClickType getClickType() {
        return clickType.get();
    }

    public Container getInventory() {
        return inventory.get();
    }

    public InventoryAction getAction() {
        return action.get();
    }

    public int getHotbarButton() {
        return hotbarButton.get();
    }

    public int getSlot() {
        return slot.get();
    }

    public SlotType getSlotType() {
        return slotType.get();
    }

    public int getRawSlot() {
        return rawSlot.get();
    }

    public Result getResult() {
        return result.get();
    }

    public void setResult(Result result) {
        this.result.set(result);
    }
}
