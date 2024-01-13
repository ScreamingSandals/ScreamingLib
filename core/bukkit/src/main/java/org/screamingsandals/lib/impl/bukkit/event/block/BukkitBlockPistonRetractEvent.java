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

package org.screamingsandals.lib.impl.bukkit.event.block;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockPlacement;
import org.screamingsandals.lib.impl.bukkit.block.BukkitBlockPlacement;
import org.screamingsandals.lib.event.block.BlockPistonRetractEvent;
import org.screamingsandals.lib.impl.utils.collections.CollectionLinkedToCollection;

import java.util.Collection;

public class BukkitBlockPistonRetractEvent extends BukkitBlockPistonEvent implements BlockPistonRetractEvent {
    // Internal cache
    private @Nullable Collection<@NotNull BlockPlacement> pushedBlocks;

    public BukkitBlockPistonRetractEvent(@NotNull org.bukkit.event.block.BlockPistonRetractEvent event) {
        super(event);
    }

    @Override
    public @NotNull Collection<@NotNull BlockPlacement> pushedBlocks() {
        if (pushedBlocks == null) {
            pushedBlocks = new CollectionLinkedToCollection<>(((org.bukkit.event.block.BlockPistonRetractEvent) event()).getBlocks(), o -> o.as(org.bukkit.block.Block.class), BukkitBlockPlacement::new);
        }
        return pushedBlocks;
    }
}
