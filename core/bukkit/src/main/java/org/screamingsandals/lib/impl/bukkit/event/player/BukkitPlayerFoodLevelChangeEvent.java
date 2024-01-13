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

import lombok.*;
import lombok.experimental.Accessors;

import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.impl.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.impl.bukkit.item.BukkitItem;
import org.screamingsandals.lib.event.player.PlayerFoodLevelChangeEvent;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.player.Player;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitPlayerFoodLevelChangeEvent implements PlayerFoodLevelChangeEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull FoodLevelChangeEvent event;

    // Internal cache
    private @Nullable Player player;
    private @Nullable ItemStack item;
    private boolean itemCached;

    @Override
    public @NotNull Player player() {
        if (player == null) {
            player = new BukkitPlayer((org.bukkit.entity.Player) event.getEntity());
        }
        return player;
    }

    @Override
    public @Nullable ItemStack item() {
        if (!itemCached) {
            if (event.getItem() != null) {
                item = new BukkitItem(event.getItem());
            }
            itemCached = true;
        }
        return item;
    }

    @Override
    public int foodLevel() {
        return event.getFoodLevel();
    }

    @Override
    public void foodLevel(int foodLevel) {
        event.setFoodLevel(foodLevel);
    }
}
