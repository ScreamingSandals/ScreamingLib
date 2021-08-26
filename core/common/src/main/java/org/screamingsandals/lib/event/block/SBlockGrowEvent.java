package org.screamingsandals.lib.event.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.state.BlockStateHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SBlockGrowEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<BlockHolder> block;
    private final ImmutableObjectLink<BlockStateHolder> newBlockState;

    public BlockHolder getBlock() {
        return block.get();
    }

    public BlockStateHolder getNewBlockState() {
        return newBlockState.get();
    }
}
