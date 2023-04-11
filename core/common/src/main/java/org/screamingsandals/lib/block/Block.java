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

package org.screamingsandals.lib.block;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.api.Wrapper;
import org.screamingsandals.lib.tasker.ThreadProperty;
import org.screamingsandals.lib.world.Location;
import org.screamingsandals.lib.block.state.BlockSnapshot;

/**
 * A class representing a block at a specific location.
 */
public interface Block extends Wrapper, ThreadProperty {
    /**
     * Sets this block to a new material.
     *
     * @param type new material
     */
    void type(@NotNull BlockType type);

    /**
     * Sets this block to a new material without applying physics.
     *
     * @param type new material
     */
    @ApiStatus.Experimental
    void alterTypeWithoutPhysics(@NotNull BlockType type);

    /**
     * Gets the current material at the location of this block.
     *
     * @return current material
     */
    @NotNull BlockType type();

    /**
     * Gets the {@link BlockSnapshot} of this block.
     *
     * @return the block snapshot, empty if there is none
     */
    @Nullable BlockSnapshot blockSnapshot();

    /**
     * Breaks this block naturally.
     */
    void breakNaturally();

    /**
     * Location of this block.
     *
     * @return the block location
     */
    @NotNull Location location();

    /**
     * Determines if this block is empty (is of the minecraft:air material or its derivatives).
     *
     * @return is this block empty?
     */
    boolean isEmpty();
}
