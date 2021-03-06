package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.event.block.SBlockIgniteEvent;

public class BlockIgniteEventListener extends AbstractBukkitEventHandlerFactory<BlockIgniteEvent, SBlockIgniteEvent> {

    public BlockIgniteEventListener(Plugin plugin) {
        super(BlockIgniteEvent.class, SBlockIgniteEvent.class, plugin);
    }

    @Override
    protected SBlockIgniteEvent wrapEvent(BlockIgniteEvent event, EventPriority priority) {
        return new SBlockIgniteEvent(
                BlockMapper.wrapBlock(event.getBlock()),
                SBlockIgniteEvent.IgniteCause.valueOf(event.getCause().name()),
                event.getIgnitingBlock() != null ? BlockMapper.wrapBlock(event.getIgnitingBlock()) : null,
                event.getIgnitingEntity() != null ? EntityMapper.wrapEntity(event.getIgnitingEntity()).orElseThrow() : null
        );
    }
}
