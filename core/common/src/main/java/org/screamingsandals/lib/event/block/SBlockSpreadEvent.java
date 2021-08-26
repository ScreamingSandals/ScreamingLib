package org.screamingsandals.lib.event.block;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.state.BlockStateHolder;

@EqualsAndHashCode(callSuper = true)
@Getter
public class SBlockSpreadEvent extends SBlockFormEvent {
    private final ImmutableObjectLink<BlockHolder> source;

    public SBlockSpreadEvent(ImmutableObjectLink<BlockHolder> block, ImmutableObjectLink<BlockHolder> source, ImmutableObjectLink<BlockStateHolder> newBlockState) {
        super(block, newBlockState);
        this.source = source;
    }

    public BlockHolder getSource() {
        return source.get();
    }
}
