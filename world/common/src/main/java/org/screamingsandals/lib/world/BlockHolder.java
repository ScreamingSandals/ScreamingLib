package org.screamingsandals.lib.world;

import lombok.Data;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.Optional;

@Data
public class BlockHolder implements Wrapper {
    private final LocationHolder location;
    private MaterialHolder type;
    private BlockDataHolder blockData;

    public BlockHolder(LocationHolder location, MaterialHolder type) {
        this.location = location;
        this.type = type;
    }

    /**
     * Sets new material at this location
     *
     * @param type type to set
     */
    public void setType(MaterialHolder type) {
        BlockMapping.setBlockAt(location, type);
        this.type = type;
    }

    /**
     * Changes state of this block to new one
     *
     * @param data data to change
     */
    public void setBlockData(BlockDataHolder data) {
        BlockDataMapping.setBlockDataAt(location, data);
        this.blockData = data;
    }

    /**
     * @return current block at this BlockHolder's location
     */
    public MaterialHolder getCurrentType() {
        final var toReturn = BlockMapping.getBlockAt(location).getType();
        this.type = toReturn;
        return toReturn;
    }

    /**
     * Gets BlockData.
     *
     * @return {@link Optional#empty()} if none is found
     */
    public Optional<BlockDataHolder> getBlockData() {
        return Optional.ofNullable(blockData);
    }

    /**
     * Gets updated BlockData
     *
     * @return {@link Optional#empty()} if none is found
     */
    public Optional<BlockDataHolder> getCurrentBlockData() {
        final var toReturn = BlockDataMapping.getBlockDataAt(location);
        toReturn.ifPresent(data -> this.blockData = data);
        return toReturn;
    }

    @Override
    public <T> T as(Class<T> type) {
        return BlockMapping.convert(this, type);
    }
}