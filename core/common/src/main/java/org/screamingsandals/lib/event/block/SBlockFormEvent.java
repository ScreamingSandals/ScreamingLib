package org.screamingsandals.lib.event.block;

import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.state.BlockStateHolder;

public class SBlockFormEvent extends SBlockGrowEvent {
    public SBlockFormEvent(ImmutableObjectLink<BlockHolder> block, ImmutableObjectLink<BlockStateHolder> newBlockState) {
        super(block, newBlockState);
    }
}
