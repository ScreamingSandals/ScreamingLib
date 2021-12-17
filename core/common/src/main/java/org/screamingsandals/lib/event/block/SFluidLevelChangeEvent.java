package org.screamingsandals.lib.event.block;

import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.block.BlockHolder;

@LimitedVersionSupport(">= 1.13.2")
public interface SFluidLevelChangeEvent extends SCancellableEvent, PlatformEventWrapper {

    BlockHolder getBlock();

    BlockTypeHolder getNewBlockData();
}
