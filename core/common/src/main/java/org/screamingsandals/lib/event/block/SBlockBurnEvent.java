package org.screamingsandals.lib.event.block;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.block.BlockHolder;

public interface SBlockBurnEvent extends SCancellableEvent {

    BlockHolder getBlock();

    @Nullable
    BlockHolder getIgnitingBlock();
}
