package org.screamingsandals.lib.event.block;

import org.screamingsandals.lib.block.BlockHolder;

import java.util.Collection;

public interface SBlockPistonRetractEvent extends SBlockPistonEvent {
    Collection<BlockHolder> getPushedBlocks();
}
