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

package org.screamingsandals.lib.impl.bukkit.event.player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.item.BukkitItem;
import org.screamingsandals.lib.event.player.PlayerArmorStandManipulateEvent;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.slot.EquipmentSlot;

public class BukkitPlayerArmorStandManipulateEvent extends BukkitPlayerInteractEntityEvent implements PlayerArmorStandManipulateEvent {
    public BukkitPlayerArmorStandManipulateEvent(@NotNull org.bukkit.event.player.PlayerArmorStandManipulateEvent event) {
        super(event);
    }

    // Internal cache
    private @Nullable ItemStack playerItem;
    private @Nullable ItemStack armorStandItem;
    private @Nullable EquipmentSlot slot;

    @Override
    public @NotNull ItemStack playerItem() {
        if (playerItem == null) {
            playerItem = new BukkitItem(event().getPlayerItem());
        }
        return playerItem;
    }

    @Override
    public @NotNull ItemStack armorStandItem() {
        if (armorStandItem == null) {
            armorStandItem = new BukkitItem(event().getArmorStandItem());
        }
        return armorStandItem;
    }

    @Override
    public @NotNull EquipmentSlot slot() {
        if (slot == null) {
            slot = EquipmentSlot.of(event().getSlot());
        }
        return slot;
    }

    @Override
    public @NotNull org.bukkit.event.player.PlayerArmorStandManipulateEvent event() {
        return (org.bukkit.event.player.PlayerArmorStandManipulateEvent) super.event();
    }
}
