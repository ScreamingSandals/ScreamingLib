package org.screamingsandals.lib.event.block;

import lombok.*;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.world.BlockHolder;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
@Setter
public class SBlockPistonExtendEvent extends SBlockPistonEvent {
    private final Collection<BlockHolder> pushedBlocks;

    public SBlockPistonExtendEvent(BlockHolder block, boolean sticky, BlockFace direction, Collection<BlockHolder> pushedBlocks) {
        super(block, sticky, direction);
        this.pushedBlocks = pushedBlocks;
    }
}
