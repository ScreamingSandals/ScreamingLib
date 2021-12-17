package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.block.BlockHolder;

public interface SEntityCombustByBlockEvent extends SEntityCombustEvent {

    BlockHolder getCombuster();
}
