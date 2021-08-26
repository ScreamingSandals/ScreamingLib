package org.screamingsandals.lib.world.state;

import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.world.BlockDataHolder;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.world.LocationHolder;

// TODO: Metadata
public interface BlockStateHolder extends Wrapper {

    BlockTypeHolder getType();

    void setType(BlockTypeHolder type);

    BlockDataHolder getBlockData();

    void setBlockData(BlockDataHolder blockData);

    LocationHolder getLocation();

    default BlockHolder getBlock() {
        return BlockMapper.wrapBlock(getLocation());
    }

    default boolean updateBlock() {
        return updateBlock(false);
    }

    default boolean updateBlock(boolean force) {
        return updateBlock(force, true);
    }

    boolean updateBlock(boolean force, boolean applyPhysics);
}
