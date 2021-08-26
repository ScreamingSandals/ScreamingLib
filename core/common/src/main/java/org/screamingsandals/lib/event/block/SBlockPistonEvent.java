package org.screamingsandals.lib.event.block;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.block.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SBlockPistonEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<BlockHolder> block;
    @Getter(AccessLevel.NONE)
    private final ImmutableObjectLink<Boolean> sticky;
    private final ImmutableObjectLink<BlockFace> direction;

    public BlockHolder getBlock() {
        return block.get();
    }

    public boolean isSticky() {
        return sticky.get();
    }

    public BlockFace getDirection() {
        return direction.get();
    }
}
