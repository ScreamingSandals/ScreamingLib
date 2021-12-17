package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.block.BlockHolder;

public interface SEntityInteractEvent extends SCancellableEvent, PlatformEventWrapper {
    EntityBasic getEntity();

    BlockHolder getBlock();
}
