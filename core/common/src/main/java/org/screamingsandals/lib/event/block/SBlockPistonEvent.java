package org.screamingsandals.lib.event.block;

import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.block.BlockHolder;

public interface SBlockPistonEvent extends SCancellableEvent, PlatformEventWrapper {

    BlockHolder getBlock();

    boolean isSticky();

    BlockFace getDirection();
}
