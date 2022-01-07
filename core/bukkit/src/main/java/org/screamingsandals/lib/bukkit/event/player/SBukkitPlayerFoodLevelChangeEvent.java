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
import lombok.experimental.Accessors;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.event.player.SPlayerFoodLevelChangeEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.player.PlayerWrapper;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerFoodLevelChangeEvent implements SPlayerFoodLevelChangeEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final FoodLevelChangeEvent event;

    // Internal cache
    private PlayerWrapper player;
    private Item item;
    private boolean itemCached;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer((Player) event.getEntity());
        }
        return player;
    }

    @Override
    public @Nullable Item getItem() {
        if (!itemCached) {
            if (event.getItem() != null) {
                item = new BukkitItem(event.getItem());
            }
            itemCached = true;
        }
        return item;
    }

    @Override
    public int getFoodLevel() {
        return event.getFoodLevel();
    }

    @Override
    public void setFoodLevel(int foodLevel) {
        event.setFoodLevel(foodLevel);
    }
}
