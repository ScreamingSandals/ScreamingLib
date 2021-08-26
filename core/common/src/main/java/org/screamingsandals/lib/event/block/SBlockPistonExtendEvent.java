package org.screamingsandals.lib.event.block;

import lombok.*;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.block.BlockHolder;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
@Setter
public class SBlockPistonExtendEvent extends SBlockPistonEvent {
    private final Collection<BlockHolder> pushedBlocks;

    public SBlockPistonExtendEvent(ImmutableObjectLink<BlockHolder> block, ImmutableObjectLink<Boolean> sticky, ImmutableObjectLink<BlockFace> direction, Collection<BlockHolder> pushedBlocks) {
        super(block, sticky, direction);
        this.pushedBlocks = pushedBlocks;
    }
}
