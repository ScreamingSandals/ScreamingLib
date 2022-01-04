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

package org.screamingsandals.lib.world.chunk;

import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.world.WorldHolder;

/**
 * An interface representing a chunk.
 */
public interface ChunkHolder extends Wrapper, RawValueHolder {
    /**
     * Gets the X coordinate of this chunk.
     *
     * @return the X coordinate
     */
    int getX();

    /**
     * Gets the Z coordinate of this chunk.
     *
     * @return the Z coordinate
     */
    int getZ();

    /**
     * Gets the world that this chunk is present in.
     *
     * @return the world of this chunk
     */
    WorldHolder getWorld();

    /**
     * Gets a block inside this chunk at the given coordinates.
     *
     * @param x the block X coordinate inside this chunk (0 to 15)
     * @param y the block Y coordinate inside this chunk
     * @param z the block Z coordinate inside this chunk (0 to 15)
     * @return the block
     */
    BlockHolder getBlock(@Range(from = 0, to = 15) int x, int y, @Range(from = 0, to = 15) int z);

    /**
     * Gets all entities inhabiting this chunk.
     * The chunk needs to be loaded in order to be able to retrieve the entities.
     *
     * @return the entities
     */
    EntityBasic[] getEntities();

    /**
     * Determines if this chunk is loaded.
     *
     * @return is this chunk loaded?
     */
    boolean isLoaded();

    /**
     * Loads this chunk.
     *
     * @return did this chunk load successfully?
     */
    boolean load();

    /**
     * Loads this chunk and generates it, if specified.
     *
     * @param generate should this chunk generate if it isn't already?
     * @return did this chunk load successfully (if generate is false and the chunk isn't generated, will return false)?
     */
    boolean load(boolean generate);

    /**
     * Unloads this chunk.
     *
     * @return did this chunk unload successfully?
     */
    boolean unload();

    /**
     * Unloads this chunk and saves it, if specified.
     *
     * @param save should this chunk be saved?
     * @return did this chunk unload (and save, if specified) successfully?
     */
    boolean unload(boolean save);
}
