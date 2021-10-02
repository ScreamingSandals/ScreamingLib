package org.screamingsandals.lib.block.state;

import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Optional;

// TODO: Metadata
public interface BlockStateHolder extends Wrapper {

    BlockTypeHolder getType();

    void setType(BlockTypeHolder type);

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

    boolean holdsInventory();

    Optional<Container> getInventory();
}
