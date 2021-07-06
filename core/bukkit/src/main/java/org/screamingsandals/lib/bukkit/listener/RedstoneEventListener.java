package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.event.block.SRedstoneEvent;

public class RedstoneEventListener extends AbstractBukkitEventHandlerFactory<BlockRedstoneEvent, SRedstoneEvent> {

    public RedstoneEventListener(Plugin plugin) {
        super(BlockRedstoneEvent.class, SRedstoneEvent.class, plugin);
    }

    @Override
    protected SRedstoneEvent wrapEvent(BlockRedstoneEvent event, EventPriority priority) {
        return new SRedstoneEvent(
                BlockMapper.wrapBlock(event.getBlock()),
                event.getOldCurrent(),
                event.getNewCurrent()
        );
    }

    @Override
    protected void postProcess(SRedstoneEvent wrappedEvent, BlockRedstoneEvent event) {
        event.setNewCurrent(wrappedEvent.getNewCurrent());

        if (wrappedEvent.isCancelled()) {
            event.setNewCurrent(event.getOldCurrent());
        }
    }
}
