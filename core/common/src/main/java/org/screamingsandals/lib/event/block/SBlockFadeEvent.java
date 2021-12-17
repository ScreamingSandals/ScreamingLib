package org.screamingsandals.lib.event.block;

import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.state.BlockStateHolder;

public interface SBlockFadeEvent extends SCancellableEvent, PlatformEventWrapper {
    BlockHolder getBlock();

    BlockStateHolder getNewBlockState();
}
