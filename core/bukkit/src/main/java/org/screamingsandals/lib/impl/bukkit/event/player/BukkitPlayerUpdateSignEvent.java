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
import org.bukkit.event.block.SignChangeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.impl.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.block.BlockPlacement;
import org.screamingsandals.lib.impl.bukkit.BukkitCore;
import org.screamingsandals.lib.impl.bukkit.block.BukkitBlockPlacement;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.impl.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.player.PlayerUpdateSignEvent;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.spectator.Component;

import java.util.Arrays;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitPlayerUpdateSignEvent implements PlayerUpdateSignEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull SignChangeEvent event;

    // Internal cache
    private @Nullable Player player;
    private @Nullable BlockPlacement block;

    @Override
    public @NotNull Player player() {
        if (player == null) {
            player = new BukkitPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public @NotNull BlockPlacement block() {
        if (block == null) {
            block = new BukkitBlockPlacement(event.getBlock());
        }
        return block;
    }

    @Override
    public @NotNull Component @NotNull [] lines() {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            return event.lines().stream().map(AdventureBackend::wrapComponent).toArray(Component[]::new);
        } else {
            return Arrays.stream(event.getLines()).map(Component::fromLegacy).toArray(Component[]::new);
        }
    }

    @Override
    public @NotNull Component line(@Range(from = 0, to = 3) int index) {
        return lines()[index];
    }

    @Override
    public void line(@Range(from = 0, to = 3) int index, @NotNull Component component) {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            event.line(index, component.as(net.kyori.adventure.text.Component.class));
        } else {
            event.setLine(index, component.toLegacy());
        }
    }
}
