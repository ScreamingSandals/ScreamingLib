package org.screamingsandals.lib.bukkit.event.player;

import lombok.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.container.ContainerFactory;
import org.screamingsandals.lib.event.player.SPlayerInventoryClickEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ClickType;
import org.screamingsandals.lib.utils.InventoryAction;
import org.screamingsandals.lib.utils.SlotType;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerInventoryClickEvent implements SPlayerInventoryClickEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final InventoryClickEvent event;

    // Internal cache
    private PlayerWrapper player;
    private Container clickedInventory;
    private boolean clickedInventoryCached;
    private ClickType clickType;
    private Container inventory;
    private InventoryAction action;
    private SlotType slotType;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer((Player) event.getWhoClicked());
        }
        return player;
    }

    @Override
    // Mutable in Bukkit API
    public @Nullable Item getCursorItem() {
        if (event.getCursor() == null) {
            return null;
        }
        return new BukkitItem(event.getCursor());
    }

    @Override
    public @Nullable Item getCurrentItem() {
        if (event.getCurrentItem() == null) {
            return null;
        }
        return new BukkitItem(event.getCurrentItem());
    }

    @Override
    public void setCurrentItem(Item currentItem) {
        event.setCurrentItem(currentItem == null ? null : currentItem.as(ItemStack.class));
    }

    public @Nullable Container getClickedInventory() {
        if (!clickedInventoryCached) {
            if (event.getClickedInventory() != null) {
                clickedInventory = ContainerFactory.wrapContainer(event.getClickedInventory()).orElseThrow();
            }
            clickedInventoryCached = true;
        }
        return clickedInventory;
    }

    @Override
    public ClickType getClickType() {
        if (clickType == null) {
            clickType = ClickType.convert(event.getClick().name());
        }
        return clickType;
    }

    @Override
    public Container getInventory() {
        if (inventory == null) {
            inventory = ContainerFactory.wrapContainer(event.getInventory()).orElseThrow();
        }
        return inventory;
    }

    @Override
    public InventoryAction getAction() {
        if (action == null) {
            action = InventoryAction.convert(event.getAction().name());
        }
        return action;
    }

    @Override
    public int getHotbarButton() {
        return event.getHotbarButton();
    }

    @Override
    public int getSlot() {
        return event.getSlot();
    }

    @Override
    public SlotType getSlotType() {
        if (slotType == null) {
            slotType = SlotType.convert(event.getSlotType().name());
        }
        return slotType;
    }

    @Override
    public int getRawSlot() {
        return event.getRawSlot();
    }

    @Override
    public Result getResult() {
        return Result.convert(event.getResult().name());
    }

    @Override
    public void setResult(Result result) {
        event.setResult(Event.Result.valueOf(event.getResult().name()));
    }
}
