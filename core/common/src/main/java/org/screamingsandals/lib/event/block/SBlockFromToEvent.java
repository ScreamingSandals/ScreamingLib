package org.screamingsandals.lib.event.block;

import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.block.BlockHolder;

public interface SBlockFromToEvent extends SCancellableEvent {

    BlockHolder getSourceBlock();

    BlockHolder getFacedBlock();

    BlockFace getFace();
}
