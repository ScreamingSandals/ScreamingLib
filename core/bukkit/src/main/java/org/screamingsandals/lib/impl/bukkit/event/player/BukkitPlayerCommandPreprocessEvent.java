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

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.impl.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.player.PlayerCommandPreprocessEvent;
import org.screamingsandals.lib.player.Player;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitPlayerCommandPreprocessEvent implements PlayerCommandPreprocessEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.player.PlayerCommandPreprocessEvent event;

    @Override
    public @NotNull Player player() {
        return new BukkitPlayer(event.getPlayer());
    }
    @Override
    public void player(@NotNull Player player) {
        event.setPlayer(player.as(org.bukkit.entity.Player.class));
    }

    @Override
    public @NotNull String command() {
        return event.getMessage();
    }

    @Override
    public void command(@NotNull String command) {
        event.setMessage(command);
    }
}
