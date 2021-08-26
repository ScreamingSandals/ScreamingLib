package org.screamingsandals.lib.world;

import lombok.Data;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.world.state.BlockStateHolder;
import org.screamingsandals.lib.world.state.BlockStateMapper;

import java.util.Optional;

@Data
public class BlockHolder implements Wrapper {
    private final LocationHolder location;
    private BlockTypeHolder type;
    @Deprecated
    private BlockDataHolder blockData;

    public BlockHolder(LocationHolder location, BlockTypeHolder type) {
        this.location = location;
        this.type = type;
    }

    /**
     * Sets new material at this location
     *
     * @param type type to set
     */
    public void setType(BlockTypeHolder type) {
        BlockMapper.setBlockAt(location, type);
        this.type = type;
    }

    /**
     * Changes state of this block to new one
     *
     * @param data data to change
     */
    public void setBlockData(BlockDataHolder data) {
        BlockDataMapper.setBlockDataAt(location, data);
        this.blockData = data;
    }

    /**
     * @return current block at this BlockHolder's location
     */
    public BlockTypeHolder getCurrentType() {
        final var toReturn = BlockMapper.getBlockAt(location).getType();
        this.type = toReturn;
        return toReturn;
    }

    /**
     * Gets BlockData.
     *
     * @return {@link Optional#empty()} if none is found
     */
    @Deprecated
    public Optional<BlockDataHolder> getBlockData() {
        return Optional.ofNullable(blockData);
    }

    /**
     * Gets updated BlockData
     *
     * @return {@link Optional#empty()} if none is found
     */
    @Deprecated
    public Optional<BlockDataHolder> getCurrentBlockData() {
        final var toReturn = BlockDataMapper.getBlockDataAt(location);
        toReturn.ifPresent(data -> this.blockData = data);
        return toReturn;
    }

    /**
     * Gets BlockState.
     *
     * @return {@link Optional#empty()} if none is found
     */
    public <T extends BlockStateHolder> Optional<T> getBlockState() {
        return BlockStateMapper.getBlockStateFromBlock(this);
    }

    /**
     * Breaks the block
     */
    public void breakNaturally() {
        BlockMapper.breakNaturally(location);
    }

    public boolean isEmpty() {
        return type.isAir();
    }

    @Override
    public <T> T as(Class<T> type) {
        return BlockMapper.convert(this, type);
    }
}
