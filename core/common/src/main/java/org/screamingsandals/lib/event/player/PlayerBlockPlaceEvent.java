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

package org.screamingsandals.lib.event.player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.event.PlatformEvent;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.block.state.BlockSnapshot;
import org.screamingsandals.lib.slot.EquipmentSlot;

import java.util.Collection;

public interface PlayerBlockPlaceEvent extends SCancellableEvent, PlayerEvent, PlatformEvent {
    /**
     * Other blocks that were also placed by the placement of block. They may have material type of AIR. This list also includes the main block
     */
    @Unmodifiable @NotNull Collection<@NotNull BlockSnapshot> replacedBlockStates();

    /**
     * Hand used to place this block
     */
    @NotNull EquipmentSlot playerHand();

    /**
     * Placed block
     */
    @NotNull Block block();

    /**
     * Replaced block state
     */
    @NotNull BlockSnapshot replacedBlockState();

    /**
     * Item in hand
     */
    @NotNull ItemStack itemInHand();
}
