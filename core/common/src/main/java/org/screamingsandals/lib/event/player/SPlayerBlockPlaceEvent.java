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

package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.state.BlockStateHolder;

import java.util.Collection;

public interface SPlayerBlockPlaceEvent extends SCancellableEvent, SPlayerEvent, PlatformEventWrapper {
    /**
     * Other blocks that were also placed by the placement of block. They may have material type of AIR. This list also includes the main block
     */
    Collection<BlockStateHolder> getReplacedBlockStates();

    /**
     * Hand used to place this block
     */
    PlayerWrapper.Hand getPlayerHand();

    /**
     * Placed block
     */
    BlockHolder getBlock();

    /**
     * Replaced block state
     */
    BlockStateHolder getReplacedBlockState();

    /**
     * Item in hand
     */
    Item getItemInHand();
}
