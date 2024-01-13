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

import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.impl.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitBlockExperienceEvent;
import org.screamingsandals.lib.event.player.PlayerBlockBreakEvent;
import org.screamingsandals.lib.player.Player;

public class BukkitPlayerBlockBreakEvent extends BukkitBlockExperienceEvent implements PlayerBlockBreakEvent, BukkitCancellable {
    public BukkitPlayerBlockBreakEvent(@NotNull BlockBreakEvent event) {
        super(event);
    }

    // Internal cache
    private @Nullable Player player;

    @Override
    public boolean dropItems() {
        return event().isDropItems();
    }

    @Override
    public void dropItems(boolean dropItems) {
        event().setDropItems(dropItems);
    }

    @Override
    public @NotNull Player player() {
        if (player == null) {
            player = new BukkitPlayer(event().getPlayer());
        }
        return player;
    }

    @Override
    public @NotNull BlockBreakEvent event() {
        return (BlockBreakEvent) super.event();
    }
}
