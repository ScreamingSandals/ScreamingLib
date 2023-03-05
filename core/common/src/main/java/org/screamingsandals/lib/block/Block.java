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

import lombok.Data;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.api.Wrapper;
import org.screamingsandals.lib.world.Location;
import org.screamingsandals.lib.block.state.BlockSnapshot;
import org.screamingsandals.lib.block.state.BlockSnapshots;

/**
 * A class representing a block at a specific location.
 */
@Data
public class Block implements Wrapper {
    /**
     * The block location.
     */
    private final @NotNull Location location;
    /**
     * The block material.
     */
    private @NotNull BlockTypeHolder type;

    /**
     * Constructs a new BlockHolder.
     *
     * Should only be used by a {@link Blocks} internally, if you want to get a block at a specified location,
     * use {@link Blocks#getBlockAt(Location)}.
     *
     * @param location the block's location
     * @param type the block's material
     */
    @ApiStatus.Internal
    public Block(@NotNull Location location, @NotNull BlockTypeHolder type) {
        this.location = location;
        this.type = type;
    }

    /**
     * Sets this block to a new material.
     *
     * @param type new material
     */
    public void setType(@NotNull BlockTypeHolder type) {
        Blocks.setBlockAt(location, type, false);
        this.type = type;
    }

    /**
     * Sets this block to a new material without applying physics.
     *
     * @param type new material
     */
    @ApiStatus.Experimental
    public void setTypeWithoutPhysics(@NotNull BlockTypeHolder type) {
        Blocks.setBlockAt(location, type, true);
        this.type = type;
    }

    /**
     * Gets the current material at the location of this block.
     *
     * @return current material
     */
    public @NotNull BlockTypeHolder getCurrentType() {
        final var toReturn = Blocks.getBlockAt(location).getType();
        this.type = toReturn;
        return toReturn;
    }

    /**
     * Gets the {@link BlockSnapshot} of this block.
     *
     * @return the block state, empty if there is none
     */
    public <T extends BlockSnapshot> @Nullable T getBlockState() {
        return BlockSnapshots.getBlockStateFromBlock(this);
    }

    /**
     * Breaks this block naturally.
     */
    public void breakNaturally() {
        Blocks.breakNaturally(location);
    }

    /**
     * Determines if this block is empty (is of the minecraft:air material or its derivatives).
     *
     * @return is this block empty?
     */
    public boolean isEmpty() {
        return type.isAir();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        return Blocks.convert(this, type);
    }
}
