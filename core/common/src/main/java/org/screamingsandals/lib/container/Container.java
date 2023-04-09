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

package org.screamingsandals.lib.container;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.container.type.InventoryType;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.item.ItemType;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.api.Wrapper;

import java.util.List;

/**
 * An interface representing an inventory.
 */
public interface Container extends Openable, Wrapper, RawValueHolder {
    /**
     * Gets the item at the specified slot in this inventory.
     *
     * @param index the inventory slot index
     * @return the item, null if there is no item
     */
    @Nullable ItemStack getItem(int index);

    /**
     * Sets an item at the specified slot index in this inventory.
     *
     * @param index the inventory slot index
     * @param item the item
     */
    void setItem(int index, @Nullable ItemStack item);

    /**
     * Tries to add items to empty slots in this inventory.
     *
     * @param items the items
     * @return the items that didn't fit the inventory
     */
    @NotNull List<@NotNull ItemStack> addItem(@NotNull ItemStack @NotNull... items);

    /**
     * Tries to remove the supplied items from this inventory.
     *
     * @param items the items
     * @return the items that couldn't be removed
     */
    @NotNull List<@NotNull ItemStack> removeItem(@NotNull ItemStack @NotNull... items);

    /**
     * Returns all items in this inventory.
     * Individual items may be null.
     *
     * @return contents of this inventory
     */
    @Nullable ItemStack @NotNull [] getContents();

    /**
     * Returns the contents from the section of this inventory where items can be stored.
     * In most cases, this will mean the entire inventory. Individual items may be null.
     *
     * @return storage contents of this inventory
     */
    @Nullable ItemStack @NotNull [] getStorageContents();

    /**
     * Sets the complete contents of this inventory to the supplied items.
     *
     * @param items the items
     * @throws IllegalArgumentException when the size of the items array doesn't match the inventory size
     */
    void setContents(@Nullable ItemStack @NotNull [] items) throws IllegalArgumentException;

    /**
     * Sets the contents of the storable section of this inventory to the supplied items.
     *
     * @param items the items
     * @throws IllegalArgumentException when the size of the items array doesn't match the inventory size
     */
    void setStorageContents(@Nullable ItemStack @NotNull [] items) throws IllegalArgumentException;

    /**
     * Checks if the supplied item type is present in this inventory.
     *
     * @param materialHolder the item type
     * @return is the supplied item type present in this inventory?
     */
    boolean contains(@NotNull ItemType materialHolder);

    /**
     * Checks if the supplied item is present in this inventory.
     *
     * @param item the item
     * @return is the supplied item present in this inventory?
     */
    boolean contains(@NotNull ItemStack item);

    /**
     * Checks if the amount of occurrences of the supplied item in this inventory is higher than or equal to the supplied amount.
     *
     * @param item the item
     * @param amount the amount
     * @return is the amount of occurrences at least the supplied amount?
     */
    boolean containsAtLeast(@NotNull ItemStack item, int amount);

    /**
     * Gets the size of this inventory.
     *
     * @return the size of this inventory
     */
    int getSize();

    /**
     * Checks if this inventory is empty.
     *
     * @return is this inventory empty?
     */
    boolean isEmpty();

    /**
     * Gets the type of this inventory.
     *
     * @return the type of this inventory
     */
    @NotNull InventoryType getType();

    /**
     * Completely wipes the contents of this inventory.
     */
    void clear();

    /**
     * Gets the index of the first empty slot in this inventory.
     *
     * @return the index of the first empty slot, -1 if there is no empty slot
     */
    int firstEmptySlot();
}
