package org.screamingsandals.lib.event.block;

import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.state.BlockStateHolder;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SMoistureChangeEvent extends SCancellableEvent {

    BlockHolder getBlock();

    BlockStateHolder getNewBlockState();
}