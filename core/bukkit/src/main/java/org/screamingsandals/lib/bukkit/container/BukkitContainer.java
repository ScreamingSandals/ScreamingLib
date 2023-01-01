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

package org.screamingsandals.lib.bukkit.container;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.container.type.InventoryTypeHolder;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BukkitContainer extends BasicWrapper<Inventory> implements Container {
    public BukkitContainer(@NotNull Inventory wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @Nullable Item getItem(int index) {
        var item = wrappedObject.getItem(index);
        return item == null ? null : new BukkitItem(item);
    }

    @Override
    public void setItem(int index, Item item) {
        wrappedObject.setItem(index, item.as(ItemStack.class));
    }

    @Override
    public List<Item> addItem(Item... items) {
        return wrappedObject.addItem(Arrays.stream(items).map(item -> item.as(ItemStack.class)).toArray(ItemStack[]::new))
                .values().stream().filter(Objects::nonNull).map(BukkitItem::new).collect(Collectors.toList());
    }

    @Override
    public List<Item> removeItem(Item... items) {
        return wrappedObject.removeItem(
                Arrays.stream(items)
                        .map(item -> item.as(ItemStack.class))
                        .toArray(ItemStack[]::new))
                .values()
                .stream()
                .filter(Objects::nonNull)
                .map(BukkitItem::new)
                .collect(Collectors.toList());
    }

    @Override
    public @Nullable Item @NotNull [] getContents() {
        var array = new Item[getSize()];

        var oldArray = wrappedObject.getContents();
        for (var i = 0; i < getSize(); i++) {
            array[i] = oldArray[i] == null ? null : new BukkitItem(oldArray[i]);
        }

        return array;
    }

    @Override
    public @Nullable Item @NotNull [] getStorageContents() {
        var oldArray = wrappedObject.getStorageContents();
        var array = new Item[oldArray.length];

        for (var i = 0; i < oldArray.length; i++) {
            array[i] = oldArray[i] == null ? null : new BukkitItem(oldArray[i]);
        }

        return array;
    }

    @Override
    public void setContents(@Nullable Item @NotNull [] items) throws IllegalArgumentException {
        if (items.length != getSize()) {
            throw new IllegalArgumentException("Wrong size of items array. Must be " + getSize());
        }
        var array = new ItemStack[getSize()];
        for (var i = 0; i < getSize(); i++) {
            array[i] = items[i] != null ? items[i].as(ItemStack.class) : null;
        }
        wrappedObject.setContents(array);
    }

    @Override
    public void setStorageContents(@Nullable Item @NotNull [] items) throws IllegalArgumentException {
        if (items.length > getSize()) {
            throw new IllegalArgumentException("Wrong size of items array. Must be " + getSize());
        }
        var array = new ItemStack[items.length];
        for (var i = 0; i < array.length; i++) {
            array[i] = items[i] != null ? items[i].as(ItemStack.class) : null;
        }
        wrappedObject.setStorageContents(array);
    }

    @Override
    public boolean contains(ItemTypeHolder materialHolder) {
        return wrappedObject.contains(materialHolder.as(Material.class));
    }

    @Override
    public boolean contains(Item item) {
        return wrappedObject.contains(item.as(ItemStack.class));
    }

    @Override
    public boolean containsAtLeast(Item item, int amount) {
        return wrappedObject.containsAtLeast(item.as(ItemStack.class), amount);
    }

    @Override
    public int getSize() {
        return wrappedObject.getSize();
    }

    @Override
    public boolean isEmpty() {
        return wrappedObject.isEmpty();
    }

    @Override
    public InventoryTypeHolder getType() {
        return InventoryTypeHolder.of(wrappedObject.getType());
    }

    @Override
    public void clear() {
        wrappedObject.clear();
    }

    @Override
    public int firstEmptySlot() {
        return wrappedObject.firstEmpty();
    }

    @Override
    public void openInventory(PlayerWrapper wrapper) {
        wrapper.asOptional(Player.class).ifPresent(player ->
                player.openInventory(wrappedObject)
        );
    }
}
