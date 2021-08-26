package org.screamingsandals.lib.event.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.block.BlockHolder;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class SBlockExplodeEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<BlockHolder> block;
    private final Collection<BlockHolder> destroyed;
    private final ObjectLink<Float> yield;

    public BlockHolder getBlock() {
        return block.get();
    }

    public float getYield() {
        return yield.get();
    }

    public void setYield(float yield) {
        this.yield.set(yield);
    }
}
