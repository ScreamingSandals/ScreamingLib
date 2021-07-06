package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.block.Block;
import org.bukkit.event.block.*;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.block.SBlockPistonEvent;
import org.screamingsandals.lib.event.block.SBlockPistonExtendEvent;
import org.screamingsandals.lib.event.block.SBlockPistonRetractEvent;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;
import org.screamingsandals.lib.world.BlockMapper;

public class BlockPistonEventListener extends AbstractBukkitEventHandlerFactory<BlockPistonEvent, SBlockPistonEvent> {

    public BlockPistonEventListener(Plugin plugin) {
        super(BlockPistonEvent.class, SBlockPistonEvent.class, plugin);
    }

    @Override
    protected SBlockPistonEvent wrapEvent(BlockPistonEvent event, EventPriority priority) {
        if (event instanceof BlockPistonExtendEvent) {
            return new SBlockPistonExtendEvent(
                    BlockMapper.wrapBlock(event.getBlock()),
                    event.isSticky(),
                    BlockFace.valueOf(event.getDirection().name()),
                    new CollectionLinkedToCollection<>(((BlockPistonExtendEvent) event).getBlocks(), o -> o.as(Block.class), BlockMapper::wrapBlock)
            );
        }

        if (event instanceof BlockPistonRetractEvent) {
            return new SBlockPistonRetractEvent(
                    BlockMapper.wrapBlock(event.getBlock()),
                    event.isSticky(),
                    BlockFace.valueOf(event.getDirection().name()),
                    new CollectionLinkedToCollection<>(((BlockPistonRetractEvent) event).getBlocks(), o -> o.as(Block.class), BlockMapper::wrapBlock)
            );
        }

        return new SBlockPistonEvent(
                BlockMapper.wrapBlock(event.getBlock()),
                event.isSticky(),
                BlockFace.valueOf(event.getDirection().name())
        );
    }
}
