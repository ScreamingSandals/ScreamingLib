package org.screamingsandals.lib.world;

import lombok.Data;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.utils.Wrapper;

@Data
public class BlockHolder implements Wrapper {
    private final LocationHolder location;
    private final MaterialHolder getOriginalBlock;

    /**
     * Sets new material at this location
     *
     * @param material material to set
     */
    public void setBlock(MaterialHolder material) {
        BlockMapping.setBlockAt(location, material);
    }

    /**
     * @return current block at this BlockHolder's location
     */
    public MaterialHolder getBlock() {
        return BlockMapping.getBlockAt(location).getGetOriginalBlock();
    }

    @Override
    public <T> T as(Class<T> type) {
        return BlockMapping.convert(this, type);
    }
}
