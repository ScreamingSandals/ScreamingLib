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

import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.event.player.SPlayerArmorStandManipulateEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;

public class SBukkitPlayerArmorStandManipulateEvent extends SBukkitPlayerInteractEntityEvent implements SPlayerArmorStandManipulateEvent {
    public SBukkitPlayerArmorStandManipulateEvent(PlayerArmorStandManipulateEvent event) {
        super(event);
    }

    // Internal cache
    private Item playerItem;
    private Item armorStandItem;
    private EquipmentSlotHolder slot;

    @Override
    public Item playerItem() {
        if (playerItem == null) {
            playerItem = new BukkitItem(event().getPlayerItem());
        }
        return playerItem;
    }

    @Override
    public Item armorStandItem() {
        if (armorStandItem == null) {
            armorStandItem = new BukkitItem(event().getArmorStandItem());
        }
        return armorStandItem;
    }

    @Override
    public EquipmentSlotHolder slot() {
        if (slot == null) {
            slot = EquipmentSlotHolder.of(event().getSlot());
        }
        return slot;
    }

    @Override
    public PlayerArmorStandManipulateEvent event() {
        return (PlayerArmorStandManipulateEvent) super.event();
    }
}
