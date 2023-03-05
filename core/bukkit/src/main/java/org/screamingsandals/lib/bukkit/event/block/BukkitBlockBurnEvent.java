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

package org.screamingsandals.lib.bukkit.event.block;

import lombok.*;
import lombok.experimental.Accessors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.bukkit.block.BukkitBlock;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.block.BlockBurnEvent;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitBlockBurnEvent implements BlockBurnEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.block.BlockBurnEvent event;

    // Internal cache
    private @Nullable Block block;
    private @Nullable Block ignitingBlock;
    private boolean ignitingBlockCached;

    @Override
    public @NotNull Block block() {
        if (block == null) {
            block = new BukkitBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public @Nullable Block ignitingBlock() {
        if (!ignitingBlockCached) {
            if (event.getIgnitingBlock() != null) {
                ignitingBlock = new BukkitBlock(event.getIgnitingBlock());
            }
            ignitingBlockCached = true;
        }
        return ignitingBlock;
    }
}
