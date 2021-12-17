package org.screamingsandals.lib.event.block;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.block.BlockHolder;

public interface SBlockShearEntityEvent extends SCancellableEvent, PlatformEventWrapper {
    BlockHolder getBlock();

    EntityBasic getEntity();

    Item getTool();
}
