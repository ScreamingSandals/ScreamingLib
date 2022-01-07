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

package org.screamingsandals.lib.bukkit.event.world;

import lombok.*;
import lombok.experimental.Accessors;

import org.bukkit.block.BlockState;
import org.bukkit.event.block.SpongeAbsorbEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.block.state.BlockStateHolder;
import org.screamingsandals.lib.block.state.BlockStateMapper;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.world.SSpongeAbsorbEvent;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;

import java.util.Collection;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitSpongeAbsorbEvent implements SSpongeAbsorbEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final SpongeAbsorbEvent event;

    // Internal cache
    private BlockHolder block;
    private Collection<BlockStateHolder> waterBlocks;

    @Override
    public BlockHolder block() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public Collection<BlockStateHolder> waterBlocks() {
        if (waterBlocks == null) {
            waterBlocks = new CollectionLinkedToCollection<>(event.getBlocks(), o -> o.as(BlockState.class), o -> BlockStateMapper.wrapBlockState(o).orElseThrow());
        }
        return waterBlocks;
    }
}
