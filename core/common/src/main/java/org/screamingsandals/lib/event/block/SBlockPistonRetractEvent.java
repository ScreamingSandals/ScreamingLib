package org.screamingsandals.lib.event.block;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.world.BlockHolder;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
@Setter
public class SBlockPistonRetractEvent extends SBlockPistonEvent {
    private final Collection<BlockHolder> pushedBlocks;

    public SBlockPistonRetractEvent(BlockHolder block, boolean sticky, BlockFace direction, Collection<BlockHolder> pushedBlocks) {
        super(block, sticky, direction);
        this.pushedBlocks = pushedBlocks;
    }
}
