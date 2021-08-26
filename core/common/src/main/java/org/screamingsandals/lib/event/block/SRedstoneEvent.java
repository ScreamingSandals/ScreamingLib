package org.screamingsandals.lib.event.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.block.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class SRedstoneEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<BlockHolder> block;
    private final ImmutableObjectLink<@Range(from = 0, to = 15) Integer> oldCurrent;
    private final ObjectLink<@Range(from = 0, to = 15) Integer> newCurrent;

    public BlockHolder getBlock() {
        return block.get();
    }

    @Range(from = 0, to = 15)
    public int getOldCurrent() {
        return oldCurrent.get();
    }

    @Range(from = 0, to = 15)
    public int getNewCurrent() {
        return newCurrent.get();
    }

    public void setNewCurrent(@Range(from = 0, to = 15) int newCurrent) {
        this.newCurrent.set(newCurrent);
    }
}
