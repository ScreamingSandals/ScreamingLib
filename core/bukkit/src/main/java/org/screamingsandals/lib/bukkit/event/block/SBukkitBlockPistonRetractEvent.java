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

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.event.block.SBlockPistonRetractEvent;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;

import java.util.Collection;

public class SBukkitBlockPistonRetractEvent extends SBukkitBlockPistonEvent implements SBlockPistonRetractEvent {
    // Internal cache
    private Collection<BlockHolder> pushedBlocks;

    public SBukkitBlockPistonRetractEvent(BlockPistonRetractEvent event) {
        super(event);
    }

    @Override
    public Collection<BlockHolder> getPushedBlocks() {
        if (pushedBlocks == null) {
            pushedBlocks = new CollectionLinkedToCollection<>(((BlockPistonRetractEvent) getEvent()).getBlocks(), o -> o.as(Block.class), BlockMapper::wrapBlock);
        }
        return pushedBlocks;
    }
}
