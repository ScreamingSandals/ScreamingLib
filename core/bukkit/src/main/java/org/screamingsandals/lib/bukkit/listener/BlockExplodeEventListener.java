package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.event.block.SBlockExplodeEvent;

public class BlockExplodeEventListener extends AbstractBukkitEventHandlerFactory<BlockExplodeEvent, SBlockExplodeEvent> {

    public BlockExplodeEventListener(Plugin plugin) {
        super(BlockExplodeEvent.class, SBlockExplodeEvent.class, plugin);
    }

    @Override
    protected SBlockExplodeEvent wrapEvent(BlockExplodeEvent event, EventPriority priority) {
        return new SBlockExplodeEvent(
                BlockMapper.wrapBlock(event.getBlock()),
                new CollectionLinkedToCollection<>(event.blockList(), o -> o.as(Block.class), BlockMapper::wrapBlock), // because it's mutable
                event.getYield()
        );
    }

    @Override
    protected void postProcess(SBlockExplodeEvent wrappedEvent, BlockExplodeEvent event) {
        event.setYield(wrappedEvent.getYield());
    }
}
