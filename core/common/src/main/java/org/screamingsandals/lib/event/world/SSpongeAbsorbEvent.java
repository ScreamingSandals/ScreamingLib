package org.screamingsandals.lib.event.world;

import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.state.BlockStateHolder;

import java.util.Collection;

@LimitedVersionSupport("Bukkit >= 1.13")
public interface SSpongeAbsorbEvent extends SCancellableEvent {

    BlockHolder getBlock();

    Collection<BlockStateHolder> getWaterBlocks();
}
