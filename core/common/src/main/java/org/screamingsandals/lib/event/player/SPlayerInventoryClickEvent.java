package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.*;

@EqualsAndHashCode(callSuper = false)
public class SPlayerInventoryClickEvent extends SPlayerCancellableEvent {
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

    public SPlayerInventoryClickEvent(ImmutableObjectLink<PlayerWrapper> player,
                                      ObjectLink<@Nullable Item> cursorItem,
                                      ObjectLink<@Nullable Item> currentItem,
                                      ImmutableObjectLink<@Nullable Container> container,
                                      ImmutableObjectLink<ClickType> clickType,
                                      ImmutableObjectLink<Container> inventory,
                                      ImmutableObjectLink<InventoryAction> action,
                                      ImmutableObjectLink<Integer> hotbarButton,
                                      ImmutableObjectLink<Integer> slot,
                                      ImmutableObjectLink<SlotType> slotType,
                                      ImmutableObjectLink<Integer> rawSlot,
                                      ObjectLink<Result> result) {
        super(player);
        this.cursorItem = cursorItem;
        this.currentItem = currentItem;
        this.container = container;
        this.clickType = clickType;
        this.inventory = inventory;
        this.action = action;
        this.hotbarButton = hotbarButton;
        this.slot = slot;
        this.slotType = slotType;
        this.rawSlot = rawSlot;
        this.result = result;
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
