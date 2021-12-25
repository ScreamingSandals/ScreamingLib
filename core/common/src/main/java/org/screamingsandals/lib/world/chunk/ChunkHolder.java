package org.screamingsandals.lib.world.chunk;

import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.world.WorldHolder;

/**
 * <p>An interface representing a chunk.</p>
 */
public interface ChunkHolder extends Wrapper {
    /**
     * <p>Gets the X coordinate of this chunk.</p>
     *
     * @return the X coordinate
     */
    int getX();

    /**
     * <p>Gets the Z coordinate of this chunk.</p>
     *
     * @return the Z coordinate
     */
    int getZ();

    /**
     * <p>Gets the world that this chunk is present in.</p>
     *
     * @return the world of this chunk
     */
    WorldHolder getWorld();

    /**
     * <p>Gets a block inside this chunk at the given coordinates.</p>
     *
     * @param x the block X coordinate inside this chunk (0 to 15)
     * @param y the block Y coordinate inside this chunk
     * @param z the block Z coordinate inside this chunk (0 to 15)
     * @return the block
     */
    BlockHolder getBlock(@Range(from = 0, to = 15) int x, int y, @Range(from = 0, to = 15) int z);

    /**
     * <p>Gets all entities inhabiting this chunk.</p>
     * <p>The chunk needs to be loaded in order to be able to retrieve the entities.</p>
     *
     * @return the entities
     */
    EntityBasic[] getEntities();

    /**
     * <p>Determines if this chunk is loaded.</p>
     *
     * @return is this chunk loaded?
     */
    boolean isLoaded();

    /**
     * <p>Loads this chunk.</p>
     *
     * @return did this chunk load successfully?
     */
    boolean load();

    /**
     * <p>Loads this chunk and generates it, if specified.</p>
     *
     * @param generate should this chunk generate if it isn't already?
     * @return did this chunk load successfully (if generate is false and the chunk isn't generated, will return false)?
     */
    boolean load(boolean generate);

    /**
     * <p>Unloads this chunk.</p>
     *
     * @return did this chunk unload successfully?
     */
    boolean unload();

    /**
     * <p>Unloads this chunk and saves it, if specified.</p>
     *
     * @param save should this chunk be saved?
     * @return did this chunk unload (and save, if specified) successfully?
     */
    boolean unload(boolean save);
}
