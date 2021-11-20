package org.screamingsandals.lib.event.block;

import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.block.BlockHolder;

public interface SRedstoneEvent extends SCancellableEvent {

    BlockHolder getBlock();

    @Range(from = 0, to = 15)
    int getOldCurrent();

    @Range(from = 0, to = 15)
    int getNewCurrent();

    void setNewCurrent(@Range(from = 0, to = 15) int newCurrent);
}
