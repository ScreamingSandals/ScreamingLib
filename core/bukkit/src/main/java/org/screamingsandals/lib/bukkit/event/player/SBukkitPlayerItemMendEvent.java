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
import org.bukkit.event.player.PlayerItemMendEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityExperience;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.entity.EntityExperience;
import org.screamingsandals.lib.event.player.SPlayerItemMendEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.player.PlayerWrapper;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerItemMendEvent implements SPlayerItemMendEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerItemMendEvent event;

    // Internal cache
    private PlayerWrapper player;
    private Item item;
    private EntityExperience experienceOrb;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public Item getItem() {
        if (item == null) {
            item = new BukkitItem(event.getItem());
        }
        return item;
    }

    @Override
    public EntityExperience getExperienceOrb() {
        if (experienceOrb == null) {
            experienceOrb = new BukkitEntityExperience(event.getExperienceOrb());
        }
        return experienceOrb;
    }

    @Override
    public int getRepairAmount() {
        return event.getRepairAmount();
    }

    @Override
    public void setRepairAmount(int repairAmount) {
        event.setRepairAmount(repairAmount);
    }
}
