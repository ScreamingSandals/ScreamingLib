/*
 * Copyright 2023 ScreamingSandals
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

import lombok.experimental.ExtensionMethod;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.container.ContainerFactory;
import org.screamingsandals.lib.event.player.PlayerInventoryClickEvent;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.utils.ClickType;
import org.screamingsandals.lib.utils.InventoryAction;
import org.screamingsandals.lib.utils.SlotType;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class BukkitPlayerInventoryClickEvent implements PlayerInventoryClickEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull InventoryClickEvent event;

    // Internal cache
    private @Nullable Player player;
    private @Nullable Container clickedInventory;
    private boolean clickedInventoryCached;
    private @Nullable ClickType clickType;
    private @Nullable Container inventory;
    private @Nullable InventoryAction action;
    private @Nullable SlotType slotType;

    @Override
    public @NotNull Player player() {
        if (player == null) {
            player = new BukkitPlayer((org.bukkit.entity.Player) event.getWhoClicked());
        }
        return player;
    }

    @Override
    // Mutable in Bukkit API
    public @Nullable ItemStack cursorItem() {
        if (event.getCursor() == null) {
            return null;
        }
        return new BukkitItem(event.getCursor());
    }

    @Override
    public @Nullable ItemStack currentItem() {
        if (event.getCurrentItem() == null) {
            return null;
        }
        return new BukkitItem(event.getCurrentItem());
    }

    @Override
    public void currentItem(@Nullable ItemStack currentItem) {
        event.setCurrentItem(currentItem == null ? null : currentItem.as(org.bukkit.inventory.ItemStack.class));
    }

    public @Nullable Container clickedInventory() {
        if (!clickedInventoryCached) {
            if (event.getClickedInventory() != null) {
                clickedInventory = ContainerFactory.<Container>wrapContainer(event.getClickedInventory()).orElseThrow();
            }
            clickedInventoryCached = true;
        }
        return clickedInventory;
    }

    @Override
    public @NotNull ClickType clickType() {
        if (clickType == null) {
            clickType = ClickType.convert(event.getClick().name());
        }
        return clickType;
    }

    @Override
    public @NotNull Container inventory() {
        if (inventory == null) {
            inventory = ContainerFactory.<Container>wrapContainer(event.getInventory()).orElseThrow();
        }
        return inventory;
    }

    @Override
    public @NotNull InventoryAction action() {
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
    public @NotNull SlotType slotType() {
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
    public @NotNull Result result() {
        return Result.convert(event.getResult().name());
    }

    @Override
    public void result(@NotNull Result result) {
        event.setResult(Event.Result.valueOf(event.getResult().name()));
    }
}
