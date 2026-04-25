/*
 * Copyright 2026 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.event.player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.container.ContainerFactory;
import org.screamingsandals.lib.event.player.PlayerInventoryDragEvent;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.impl.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.impl.bukkit.item.BukkitItem;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.player.Player;

import java.util.Objects;
import java.util.Set;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitPlayerInventoryDragEvent implements PlayerInventoryDragEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull InventoryDragEvent event;

    // Internal cache
    private @Nullable Player player;
    private @Nullable ItemStack oldCursor;
    private @Nullable Container inventory;

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
    public void cursorItem(@Nullable ItemStack currentItem) {
        event.setCursor(currentItem == null ? null : currentItem.as(org.bukkit.inventory.ItemStack.class));
    }

    @Override
    public @NotNull ItemStack oldCursorItem() {
        if (oldCursor == null) {
            oldCursor = new BukkitItem(event.getOldCursor());
        }
        return oldCursor;
    }

    @Override
    public @NotNull Container inventory() {
        if (inventory == null) {
            inventory = Objects.requireNonNull(ContainerFactory.wrapContainer(event.getInventory()));
        }
        return inventory;
    }

    @Override
    public @NotNull Set<@NotNull Integer> rawSlots() {
        return event.getRawSlots();
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
