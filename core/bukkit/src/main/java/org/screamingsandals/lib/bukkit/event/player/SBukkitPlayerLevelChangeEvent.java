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

import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.event.player.SPlayerLevelChangeEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerLevelChangeEvent implements SPlayerLevelChangeEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerLevelChangeEvent event;

    // Internal cache
    private PlayerWrapper player;

    @Override
    public PlayerWrapper player() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public int oldLevel() {
        return event.getOldLevel();
    }

    @Override
    public int newLevel() {
        return event.getNewLevel();
    }
}
