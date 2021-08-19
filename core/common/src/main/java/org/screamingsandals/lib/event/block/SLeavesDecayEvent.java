package org.screamingsandals.lib.event.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SLeavesDecayEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<BlockHolder> block;

    public BlockHolder getBlock() {
        return block.get();
    }
}
