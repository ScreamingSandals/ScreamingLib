package org.screamingsandals.lib.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.event.block.SBlockPistonExtendEvent;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;

import java.util.Collection;

public class SBukkitBlockPistonExtendEvent extends SBukkitBlockPistonEvent implements SBlockPistonExtendEvent {

    // Internal cache
    private Collection<BlockHolder> pushedBlocks;

    public SBukkitBlockPistonExtendEvent(BlockPistonExtendEvent event) {
        super(event);
    }

    @Override
    public Collection<BlockHolder> getPushedBlocks() {
        if (pushedBlocks == null) {
            pushedBlocks = new CollectionLinkedToCollection<>(((BlockPistonExtendEvent) getEvent()).getBlocks(), o -> o.as(Block.class), BlockMapper::wrapBlock);
        }
        return pushedBlocks;
    }
}
