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

package org.screamingsandals.lib.impl.bukkit.event.player;

import lombok.*;
import lombok.experimental.Accessors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitExperienceOrb;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.impl.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.impl.bukkit.item.BukkitItem;
import org.screamingsandals.lib.entity.ExperienceOrb;
import org.screamingsandals.lib.event.player.PlayerItemMendEvent;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.player.Player;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitPlayerItemMendEvent implements PlayerItemMendEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.player.PlayerItemMendEvent event;

    // Internal cache
    private @Nullable Player player;
    private @Nullable ItemStack item;
    private @Nullable ExperienceOrb experienceOrb;

    @Override
    public @NotNull Player player() {
        if (player == null) {
            player = new BukkitPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public @NotNull ItemStack item() {
        if (item == null) {
            item = new BukkitItem(event.getItem());
        }
        return item;
    }

    @Override
    public @NotNull ExperienceOrb experienceOrb() {
        if (experienceOrb == null) {
            experienceOrb = new BukkitExperienceOrb(event.getExperienceOrb());
        }
        return experienceOrb;
    }

    @Override
    public int repairAmount() {
        return event.getRepairAmount();
    }

    @Override
    public void repairAmount(int repairAmount) {
        event.setRepairAmount(repairAmount);
    }
}
