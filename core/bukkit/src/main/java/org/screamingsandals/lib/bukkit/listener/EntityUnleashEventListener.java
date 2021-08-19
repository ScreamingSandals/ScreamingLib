package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityUnleashEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class EntityUnleashEventListener extends AbstractBukkitEventHandlerFactory<EntityUnleashEvent, SEntityUnleashEvent> {

    public EntityUnleashEventListener(Plugin plugin) {
        super(EntityUnleashEvent.class, SEntityUnleashEvent.class, plugin);
    }

    @Override
    protected SEntityUnleashEvent wrapEvent(EntityUnleashEvent event, EventPriority priority) {
        return new SEntityUnleashEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(() -> SEntityUnleashEvent.UnleashReason.valueOf(event.getReason().name().toUpperCase()))
        );
    }
}
