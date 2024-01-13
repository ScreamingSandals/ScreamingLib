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
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityUnleashEvent;
import org.screamingsandals.lib.event.player.PlayerUnleashEntityEvent;
import org.screamingsandals.lib.player.Player;

public class BukkitPlayerUnleashEntityEvent extends BukkitEntityUnleashEvent implements PlayerUnleashEntityEvent {
    public BukkitPlayerUnleashEntityEvent(@NotNull org.bukkit.event.player.PlayerUnleashEntityEvent event) {
        super(event);
    }

    // Internal cache
    private @Nullable Player player;

    @Override
    public @NotNull Player player() {
        if (player == null) {
            player = new BukkitPlayer(((org.bukkit.event.player.PlayerUnleashEntityEvent) event()).getPlayer());
        }
        return player;
    }
}
