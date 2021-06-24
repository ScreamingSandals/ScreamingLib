package org.screamingsandals.lib.world.event;

import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.state.BlockStateHolder;

public class SBlockFormEvent extends SBlockGrowEvent {
    public SBlockFormEvent(BlockHolder block, BlockStateHolder newBlockState) {
        super(block, newBlockState);
    }
}
