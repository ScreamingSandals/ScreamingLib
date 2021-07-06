package org.screamingsandals.lib.event.block;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.state.BlockStateHolder;

@EqualsAndHashCode(callSuper = true)
@Getter
public class SBlockSpreadEvent extends SBlockFormEvent {
    private final BlockHolder source;

    public SBlockSpreadEvent(BlockHolder block, BlockHolder source, BlockStateHolder newBlockState) {
        super(block, newBlockState);
        this.source = source;
    }
}
