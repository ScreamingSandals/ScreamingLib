/*
 * Copyright 2022 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.bukkit.event.player;

import lombok.*;
import lombok.experimental.Accessors;

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

@Accessors(fluent = true)
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
    public PlayerWrapper player() {
        if (player == null) {
            player = new BukkitEntityPlayer((Player) event.getWhoClicked());
        }
        return player;
    }

    @Override
    // Mutable in Bukkit API
    public @Nullable Item cursorItem() {
        if (event.getCursor() == null) {
            return null;
        }
        return new BukkitItem(event.getCursor());
    }

    @Override
    public @Nullable Item currentItem() {
        if (event.getCurrentItem() == null) {
            return null;
        }
        return new BukkitItem(event.getCurrentItem());
    }

    @Override
    public void currentItem(Item currentItem) {
        event.setCurrentItem(currentItem == null ? null : currentItem.as(ItemStack.class));
    }

    public @Nullable Container clickedInventory() {
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
    public Container inventory() {
        if (inventory == null) {
            inventory = ContainerFactory.wrapContainer(event.getInventory()).orElseThrow();
        }
        return inventory;
    }

    @Override
    public InventoryAction action() {
        if (action == null) {
            action = InventoryAction.convert(event.getAction().name());
        }
        return action;
    }

    @Override
    public int hotbarButton() {
        return event.getHotbarButton();
    }

    @Override
    public int slot() {
        return event.getSlot();
    }

    @Override
    public SlotType slotType() {
        if (slotType == null) {
            slotType = SlotType.convert(event.getSlotType().name());
        }
        return slotType;
    }

    @Override
    public int rawSlot() {
        return event.getRawSlot();
    }

    @Override
    public Result result() {
        return Result.convert(event.getResult().name());
    }

    @Override
    public void result(Result result) {
        event.setResult(Event.Result.valueOf(event.getResult().name()));
    }
}
