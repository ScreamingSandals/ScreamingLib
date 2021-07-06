package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityEnterBlockEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityEnterBlockEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.world.BlockMapper;

public class EntityEnterBlockEventListener extends AbstractBukkitEventHandlerFactory<EntityEnterBlockEvent, SEntityEnterBlockEvent> {

    public EntityEnterBlockEventListener(Plugin plugin) {
        super(EntityEnterBlockEvent.class, SEntityEnterBlockEvent.class, plugin);
    }

    @Override
    protected SEntityEnterBlockEvent wrapEvent(EntityEnterBlockEvent event, EventPriority priority) {
        return new SEntityEnterBlockEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                BlockMapper.wrapBlock(event.getBlock())
        );
    }
}
