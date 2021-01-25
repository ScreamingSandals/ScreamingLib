package org.screamingsandals.lib.world;

import lombok.Data;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.Optional;

@Data
public class BlockHolder implements Wrapper {
    private final LocationHolder location;
    private final MaterialHolder block;
    private BlockDataHolder blockData;

    /**
     * Sets new material at this location
     *
     * @param material material to set
     */
    public void setBlock(MaterialHolder material) {
        BlockMapping.setBlockAt(location, material);
    }

    public void setState(BlockDataHolder state) {
        BlockDataMapping.setBlockStateAt(location, state);
    }

    /**
     * @return current block at this BlockHolder's location
     */
    public MaterialHolder getCurrentBlock() {
        return BlockMapping.getBlockAt(location).getBlock();
    }

    public Optional<BlockDataHolder> getBlockData() {
        return Optional.ofNullable(blockData);
    }

    public Optional<BlockDataHolder> getCurrentBlockState() {
        return BlockDataMapping.getBlockStateAt(location);
    }



    @Override
    public <T> T as(Class<T> type) {
        return BlockMapping.convert(this, type);
    }
}
