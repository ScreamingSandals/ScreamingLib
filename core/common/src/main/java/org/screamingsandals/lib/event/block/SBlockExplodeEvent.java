package org.screamingsandals.lib.event.block;

import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.block.BlockHolder;

import java.util.Collection;

public interface SBlockExplodeEvent extends SCancellableEvent {

    BlockHolder getBlock();

    Collection<BlockHolder> getDestroyed();

    float getYield();

    void setYield(float yield);
}
