package org.screamingsandals.lib.event.block;

import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.block.BlockHolder;

public interface SBlockCookEvent extends SCancellableEvent {

    BlockHolder getBlock();

    Item getSource();

    Item getResult();

    void setResult(Item item);
}
