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

import lombok.*;
import lombok.experimental.Accessors;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.NoAutoCancellable;
import org.screamingsandals.lib.event.player.SPlayerBedEnterEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerBedEnterEvent implements SPlayerBedEnterEvent, NoAutoCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerBedEnterEvent event;

    // Internal cache
    private PlayerWrapper player;
    private BlockHolder bed;
    private BedEnterResult bedEnterResult;

    @Override
    public @NotNull PlayerWrapper player() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public BlockHolder bed() {
        if (bed == null) {
            bed = BlockMapper.wrapBlock(event.getBed());
        }
        return bed;
    }

    @Override
    public BedEnterResult bedEnterResult() {
        if (bedEnterResult == null) {
            bedEnterResult = BedEnterResult.convert(event.getBedEnterResult().name());
        }
        return bedEnterResult;
    }

    @Override
    public Result useBed() {
        return Result.convert(event.useBed().name());
    }

    @Override
    public void useBed(Result useBed) {
        event.setUseBed(Event.Result.valueOf(useBed.name()));
    }
}
