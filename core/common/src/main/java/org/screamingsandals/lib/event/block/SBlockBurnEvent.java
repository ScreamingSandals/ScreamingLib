package org.screamingsandals.lib.event.block;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.block.BlockHolder;

public interface SBlockBurnEvent extends SCancellableEvent, PlatformEventWrapper {

    BlockHolder getBlock();

    @Nullable
    BlockHolder getIgnitingBlock();
}
