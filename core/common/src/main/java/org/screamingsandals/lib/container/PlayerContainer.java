/*
 * Copyright 2024 ScreamingSandals
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
import org.screamingsandals.lib.item.ItemStack;

/**
 * An interface representing a player's inventory.
 * <p>
 * A visualization of slot indexes in a player inventory: <br>
 * <img src="https://i.imgur.com/wZ1B8jC.png">
 */
public interface PlayerContainer extends Container {
    /**
     * Gets the contents of the armor slots.
     *
     * @return the armor items
     */
    @Nullable ItemStack @NotNull [] getArmorContents();

    /**
     * Gets the item in the helmet slot.
     *
     * @return the item in the helmet slot, null if there is no item
     */
    @Nullable ItemStack getHelmet();

    /**
     * Gets the item in the chestplate slot.
     *
     * @return the item in the chestplate slot, null if there is no item
     */
    @Nullable ItemStack getChestplate();

    /**
     * Gets the item in the leggings slot.
     *
     * @return the item in the leggings slot, null if there is no item
     */
    @Nullable ItemStack getLeggings();

    /**
     * Gets the item in the boots slot.
     *
     * @return the item in the boots slot, null if there is no item
     */
    @Nullable ItemStack getBoots();

    /**
     * Sets the armor slots to the supplied items.
     *
     * @param items the armor items
     */
    void setArmorContents(@Nullable ItemStack @Nullable [] items);

    /**
     * Sets the helmet slot to the supplied item.
     *
     * @param helmet the item
     */
    void setHelmet(@Nullable ItemStack helmet);

    /**
     * Sets the chestplate slot to the supplied item.
     *
     * @param chestplate the item
     */
    void setChestplate(@Nullable ItemStack chestplate);

    /**
     * Sets the leggings slot to the supplied item.
     *
     * @param leggings the item
     */
    void setLeggings(@Nullable ItemStack leggings);

    /**
     * Sets the boots slot to the supplied item.
     *
     * @param boots the item
     */
    void setBoots(@Nullable ItemStack boots);

    /**
     * Gets a copy of the item in the main hand slot.
     *
     * @return the item
     */
    @NotNull ItemStack getItemInMainHand();

    /**
     * Sets the main hand slot to the supplied item.
     *
     * @param item the item
     */
    void setItemInMainHand(@Nullable ItemStack item);

    /**
     * Gets a copy of the item in the offhand slot.
     *
     * @return the item
     */
    @NotNull ItemStack getItemInOffHand();

    /**
     * Sets the offhand slot to the supplied item.
     *
     * @param item the item
     */
    void setItemInOffHand(@Nullable ItemStack item);

    /**
     * Gets the slot index of the currently held item.
     *
     * @return the slot index
     */
    int getHeldItemSlot();

    /**
     * Sets the slot index of the currently held item.
     *
     * @param slot the new slot index
     */
    void setHeldItemSlot(int slot);
}
