package org.screamingsandals.lib.event.block;

import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.block.BlockHolder;

public interface SBlockPhysicsEvent extends SCancellableEvent, PlatformEventWrapper {
    BlockHolder getBlock();

    BlockTypeHolder getMaterial();

    BlockHolder getCausingBlock();
}
