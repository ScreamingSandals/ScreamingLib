package org.screamingsandals.lib.block;

import lombok.Data;
import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.block.state.BlockStateHolder;
import org.screamingsandals.lib.block.state.BlockStateMapper;

import java.util.Optional;

/**
 * A class representing a block at a specific location.
 */
@Data
public class BlockHolder implements Wrapper {
    /**
     * The block location.
     */
    private final LocationHolder location;
    /**
     * The block material.
     */
    private BlockTypeHolder type;

    /**
     * Constructs a new BlockHolder.
     *
     * Should only be used by a {@link BlockMapper} internally, if you want to get a block at a specified location,
     * use {@link BlockMapper#getBlockAt(LocationHolder)}.
     *
     * @param location the block's location
     * @param type the block's material
     */
    @ApiStatus.Internal
    public BlockHolder(LocationHolder location, BlockTypeHolder type) {
        this.location = location;
        this.type = type;
    }

    /**
     * Sets this block to a new material.
     *
     * @param type new material
     */
    public void setType(BlockTypeHolder type) {
        BlockMapper.setBlockAt(location, type, false);
        this.type = type;
    }

    /**
     * Sets this block to a new material without applying physics.
     *
     * @param type new material
     */
    @ApiStatus.Experimental
    public void setTypeWithoutPhysics(BlockTypeHolder type) {
        BlockMapper.setBlockAt(location, type, true);
        this.type = type;
    }

    /**
     * Gets the current material at the location of this block.
     *
     * @return current material
     */
    public BlockTypeHolder getCurrentType() {
        final var toReturn = BlockMapper.getBlockAt(location).getType();
        this.type = toReturn;
        return toReturn;
    }

    /**
     * Gets the {@link BlockStateHolder} of this block.
     *
     * @return the block state, empty if there is none
     */
    public <T extends BlockStateHolder> Optional<T> getBlockState() {
        return BlockStateMapper.getBlockStateFromBlock(this);
    }

    /**
     * Breaks this block naturally.
     */
    public void breakNaturally() {
        BlockMapper.breakNaturally(location);
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
    public <T> T as(Class<T> type) {
        return BlockMapper.convert(this, type);
    }
}
