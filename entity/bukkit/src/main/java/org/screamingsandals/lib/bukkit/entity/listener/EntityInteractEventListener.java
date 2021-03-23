package org.screamingsandals.lib.bukkit.entity.listener;

import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.event.SEntityInteractEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.world.BlockMapper;

public class EntityInteractEventListener extends AbstractBukkitEventHandlerFactory<EntityInteractEvent, SEntityInteractEvent> {

    public EntityInteractEventListener(Plugin plugin) {
        super(EntityInteractEvent.class, SEntityInteractEvent.class, plugin);
    }

    @Override
    protected SEntityInteractEvent wrapEvent(EntityInteractEvent event, EventPriority priority) {
        return new SEntityInteractEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                BlockMapper.wrapBlock(event.getBlock())
        );
    }
}
