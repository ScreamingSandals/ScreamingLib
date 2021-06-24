package org.screamingsandals.lib.bukkit.entity.listener;

import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.event.SEntityUnleashEvent;
import org.screamingsandals.lib.event.EventPriority;

public class EntityUnleashEventListener extends AbstractBukkitEventHandlerFactory<EntityUnleashEvent, SEntityUnleashEvent> {

    public EntityUnleashEventListener(Plugin plugin) {
        super(EntityUnleashEvent.class, SEntityUnleashEvent.class, plugin);
    }

    @Override
    protected SEntityUnleashEvent wrapEvent(EntityUnleashEvent event, EventPriority priority) {
        return new SEntityUnleashEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                SEntityUnleashEvent.UnleashReason.valueOf(event.getReason().name().toUpperCase())
        );
    }
}
