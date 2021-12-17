package org.screamingsandals.lib.event.block;

import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SEvent;

public interface SRedstoneEvent extends SEvent, PlatformEventWrapper {

    BlockHolder getBlock();

    @Range(from = 0, to = 15)
    int getOldCurrent();

    @Range(from = 0, to = 15)
    int getNewCurrent();

    void setNewCurrent(@Range(from = 0, to = 15) int newCurrent);
}
