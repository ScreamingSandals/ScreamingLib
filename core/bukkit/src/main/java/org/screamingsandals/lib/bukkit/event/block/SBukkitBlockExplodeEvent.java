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

package org.screamingsandals.lib.bukkit.event.block;

import lombok.*;
import lombok.experimental.Accessors;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockExplodeEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.block.SBlockExplodeEvent;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;

import java.util.Collection;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitBlockExplodeEvent implements SBlockExplodeEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final BlockExplodeEvent event;

    // Internal cache
    private BlockHolder block;
    private Collection<BlockHolder> destroyed;

    @Override
    public BlockHolder block() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public Collection<BlockHolder> destroyed() {
        if (destroyed == null) {
            destroyed = new CollectionLinkedToCollection<>(event.blockList(), o -> o.as(Block.class), BlockMapper::wrapBlock);
        }
        return destroyed;
    }

    @Override
    public float yield() {
        return event.getYield();
    }

    @Override
    public void yield(float yield) {
        event.setYield(yield);
    }
}
