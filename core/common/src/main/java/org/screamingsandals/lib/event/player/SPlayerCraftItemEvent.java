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
public class SPlayerCraftItemEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ObjectLink<Item> currentItem;
    private final ImmutableObjectLink<Container> clickedInventoryContainer;
    private final ImmutableObjectLink<Container> craftInventory;
    private final ImmutableObjectLink<ClickType> clickType;
    private final ImmutableObjectLink<Recipe> recipe;
    private final ObjectLink<Result> result;
    private final ImmutableObjectLink<InventoryAction> inventoryAction;
    private final ImmutableObjectLink<Item> cursor;
    private final ImmutableObjectLink<SlotType> slotType;
    private final ImmutableObjectLink<Integer> hotbarButton;
    private final ImmutableObjectLink<Integer> rawSlot;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    public Item getCurrentItem() {
        return currentItem.get();
    }

    public void setCurrentItem(Item currentItem) {
        this.currentItem.set(currentItem);
    }

    @Nullable
    public Container getClickedInventoryContainer() {
        return clickedInventoryContainer.get();
    }

    public Container getCraftInventory() {
        return craftInventory.get();
    }

    public ClickType getClickType() {
        return clickType.get();
    }

    public Recipe getRecipe() {
        return recipe.get();
    }

    public Result getResult() {
        return result.get();
    }

    public void setResult(Result result) {
        this.result.set(result);
    }

    public InventoryAction getInventoryAction() {
        return inventoryAction.get();
    }

    public Item getCursor() {
        return cursor.get();
    }

    public SlotType getSlotType() {
        return slotType.get();
    }

    public int getHotbarButton() {
        return hotbarButton.get();
    }

    public int getRawSlot() {
        return rawSlot.get();
    }

    @AllArgsConstructor
    @Data
    // TODO: we should use holder as there are many type of recipes
    public static class Recipe {
        private final Item result;
    }
}
