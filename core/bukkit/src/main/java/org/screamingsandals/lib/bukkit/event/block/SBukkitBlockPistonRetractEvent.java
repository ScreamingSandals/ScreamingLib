package org.screamingsandals.lib.bukkit.event.block;

import lombok.EqualsAndHashCode;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.event.block.SBlockPistonRetractEvent;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;

import java.util.Collection;

public class SBukkitBlockPistonRetractEvent extends SBukkitBlockPistonEvent implements SBlockPistonRetractEvent {
    // Internal cache
    private Collection<BlockHolder> pushedBlocks;

    public SBukkitBlockPistonRetractEvent(BlockPistonRetractEvent event) {
        super(event);
    }

    @Override
    public Collection<BlockHolder> getPushedBlocks() {
        if (pushedBlocks == null) {
            pushedBlocks = new CollectionLinkedToCollection<>(((BlockPistonRetractEvent) getEvent()).getBlocks(), o -> o.as(Block.class), BlockMapper::wrapBlock);
        }
        return pushedBlocks;
    }
}
