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

package org.screamingsandals.lib.impl.bukkit.container;

import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.item.BukkitItem;
import org.screamingsandals.lib.impl.bukkit.utils.Version;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.container.PlayerContainer;
import org.screamingsandals.lib.item.builder.ItemStackFactory;

import java.util.Arrays;

public class BukkitPlayerContainer extends BukkitContainer implements PlayerContainer {
    public BukkitPlayerContainer(@NotNull PlayerInventory wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @Nullable ItemStack @NotNull [] getArmorContents() {
        return Arrays.stream(((PlayerInventory) wrappedObject)
                .getArmorContents())
                .map(item -> item == null ? null : new BukkitItem(item)) // does your ide see problem? well bukkit api is badly annotated
                .toArray(ItemStack[]::new);
    }

    @Override
    public @Nullable ItemStack getHelmet() {
        var item = ((PlayerInventory) wrappedObject).getHelmet();
        return item == null ? null : new BukkitItem(item);
    }

    @Override
    public @Nullable ItemStack getChestplate() {
        var item = ((PlayerInventory) wrappedObject).getChestplate();
        return item == null ? null : new BukkitItem(item);
    }

    @Override
    public @Nullable ItemStack getLeggings() {
        var item = ((PlayerInventory) wrappedObject).getLeggings();
        return item == null ? null : new BukkitItem(item);
    }

    @Override
    public @Nullable ItemStack getBoots() {
        var item = ((PlayerInventory) wrappedObject).getBoots();
        return item == null ? null : new BukkitItem(item);
    }

    @Override
    public void setArmorContents(@Nullable ItemStack @Nullable [] items) {
        if (items == null) {
            ((PlayerInventory) wrappedObject).setArmorContents(null);
            return;
        }
        ((PlayerInventory) wrappedObject).setArmorContents(Arrays.stream(items).map(item -> {
            if (item == null) {
                return null;
            }
            return item.as(org.bukkit.inventory.ItemStack.class);
        }).toArray(org.bukkit.inventory.ItemStack[]::new));
    }

    @Override
    public void setHelmet(@Nullable ItemStack helmet) {
        var inventory = (PlayerInventory) wrappedObject;
        if (helmet == null) {
            inventory.setHelmet(null);
            return;
        }
        inventory.setHelmet(helmet.as(org.bukkit.inventory.ItemStack.class));
    }

    @Override
    public void setChestplate(@Nullable ItemStack chestplate) {
        var inventory = (PlayerInventory) wrappedObject;
        if (chestplate == null) {
            inventory.setChestplate(null);
            return;
        }
        inventory.setChestplate(chestplate.as(org.bukkit.inventory.ItemStack.class));
    }

    @Override
    public void setLeggings(@Nullable ItemStack leggings) {
        var inventory = (PlayerInventory) wrappedObject;
        if (leggings == null) {
            inventory.setLeggings(null);
            return;
        }
        inventory.setLeggings(leggings.as(org.bukkit.inventory.ItemStack.class));
    }

    @Override
    public void setBoots(@Nullable ItemStack boots) {
        var inventory = (PlayerInventory) wrappedObject;
        if (boots == null) {
            inventory.setBoots(null);
            return;
        }
        inventory.setBoots(boots.as(org.bukkit.inventory.ItemStack.class));
    }

    @Override
    public @NotNull ItemStack getItemInMainHand() {
        if (Version.isVersion(1, 9)) {
            return new BukkitItem(((PlayerInventory) wrappedObject).getItemInMainHand());
        } else {
            return new BukkitItem(((PlayerInventory) wrappedObject).getItemInHand());
        }
    }

    @Override
    public void setItemInMainHand(@Nullable ItemStack item) {
        var inventory = (PlayerInventory) wrappedObject;
        if (Version.isVersion(1, 9)) {
            if (item == null) {
                inventory.setItemInMainHand(null);
                return;
            }
            inventory.setItemInMainHand(item.as(org.bukkit.inventory.ItemStack.class));
        } else {
            if (item == null) {
                inventory.setItemInHand(null);
                return;
            }
            inventory.setItemInHand(item.as(org.bukkit.inventory.ItemStack.class));
        }
    }

    @Override
    public @NotNull ItemStack getItemInOffHand() {
        if (Version.isVersion(1, 9)) {
            return new BukkitItem(((PlayerInventory) wrappedObject).getItemInOffHand());
        } else {
            return ItemStackFactory.getAir();
        }
    }

    @Override
    public void setItemInOffHand(@Nullable ItemStack item) {
        var inventory = (PlayerInventory) wrappedObject;
        if (Version.isVersion(1, 9)) {
            if (item == null) {
                inventory.setItemInOffHand(null);
                return;
            }
            inventory.setItemInOffHand(item.as(org.bukkit.inventory.ItemStack.class));
        } else {
            if (item != null && !item.isAir()) {
                // I have no idea where we should put the item, so I just pass it to the addItem method and drop it in case of failure.
                var failure = inventory.addItem(item.as(org.bukkit.inventory.ItemStack.class));
                if (!failure.isEmpty()) {
                    var holder = inventory.getHolder();
                    if (holder != null) {
                        var loc = holder.getLocation();
                        failure.forEach((integer, itemStack) -> loc.getWorld().dropItem(loc, itemStack));
                    }
                }
            }
        }
    }

    @Override
    public int getHeldItemSlot() {
        return ((PlayerInventory) wrappedObject).getHeldItemSlot();
    }

    @Override
    public void setHeldItemSlot(int slot) {
        ((PlayerInventory) wrappedObject).setHeldItemSlot(slot);
    }
}
