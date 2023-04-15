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

import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockPlacement;
import org.screamingsandals.lib.impl.bukkit.block.BukkitBlockPlacement;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.event.NoAutoCancellable;
import org.screamingsandals.lib.event.player.PlayerBedEnterEvent;
import org.screamingsandals.lib.player.Player;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitPlayerBedEnterEvent implements PlayerBedEnterEvent, NoAutoCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.player.PlayerBedEnterEvent event;

    // Internal cache
    private @Nullable Player player;
    private @Nullable BlockPlacement bed;
    private @Nullable BedEnterResult bedEnterResult;

    @Override
    public @NotNull Player player() {
        if (player == null) {
            player = new BukkitPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public @NotNull BlockPlacement bed() {
        if (bed == null) {
            bed = new BukkitBlockPlacement(event.getBed());
        }
        return bed;
    }

    @Override
    public @NotNull BedEnterResult bedEnterResult() {
        if (bedEnterResult == null) {
            bedEnterResult = BedEnterResult.convert(event.getBedEnterResult().name());
        }
        return bedEnterResult;
    }

    @Override
    public @NotNull Result useBed() {
        return Result.convert(event.useBed().name());
    }

    @Override
    public void useBed(@NotNull Result useBed) {
        event.setUseBed(Event.Result.valueOf(useBed.name()));
    }
}
