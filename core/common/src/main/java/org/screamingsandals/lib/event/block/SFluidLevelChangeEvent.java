package org.screamingsandals.lib.event.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.block.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
@LimitedVersionSupport(">= 1.13.2")
public class SFluidLevelChangeEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<BlockHolder> block;
    private final ImmutableObjectLink<BlockTypeHolder> newBlockData;

    public BlockHolder getBlock() {
        return block.get();
    }

    public BlockTypeHolder getNewBlockData() {
        return newBlockData.get();
    }
}
