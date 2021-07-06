package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.event.block.SBlockExperienceEvent;

public class BlockExperienceEventListener extends AbstractBukkitEventHandlerFactory<BlockExpEvent, SBlockExperienceEvent> {

    public BlockExperienceEventListener(Plugin plugin) {
        super(BlockExpEvent.class, SBlockExperienceEvent.class, plugin);
    }

    @Override
    protected SBlockExperienceEvent wrapEvent(BlockExpEvent event, EventPriority priority) {
        if (event instanceof BlockBreakEvent) {
            return null; // already handled by PlayerBreakBlockEventListener
        }

        return new SBlockExperienceEvent(
                BlockMapper.wrapBlock(event.getBlock()),
                event.getExpToDrop()
        );
    }

    @Override
    protected void postProcess(SBlockExperienceEvent wrappedEvent, BlockExpEvent event) {
        event.setExpToDrop(wrappedEvent.getExperience());
    }
}
