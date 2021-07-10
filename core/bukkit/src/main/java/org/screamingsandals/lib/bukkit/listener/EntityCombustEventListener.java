package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityCombustByBlockEvent;
import org.screamingsandals.lib.event.entity.SEntityCombustEvent;
import org.screamingsandals.lib.event.EventPriority;

public class EntityCombustEventListener extends AbstractBukkitEventHandlerFactory<EntityCombustEvent, SEntityCombustEvent> {

    public EntityCombustEventListener(Plugin plugin) {
        super(EntityCombustEvent.class, SEntityCombustEvent.class, plugin);
    }

    @Override
    protected SEntityCombustEvent wrapEvent(EntityCombustEvent event, EventPriority priority) {
        if (event instanceof EntityCombustByBlockEvent) {
            return new SEntityCombustByBlockEvent(
                    EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                    event.getDuration()
            );
        }

        return new SEntityCombustEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                event.getDuration()
        );
    }
}
