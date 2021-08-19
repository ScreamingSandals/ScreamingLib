package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityTameEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class EntityTameEventListener extends AbstractBukkitEventHandlerFactory<EntityTameEvent, SEntityTameEvent> {

    public EntityTameEventListener(Plugin plugin) {
        super(EntityTameEvent.class, SEntityTameEvent.class, plugin);
    }

    @Override
    protected SEntityTameEvent wrapEvent(EntityTameEvent event, EventPriority priority) {
        return new SEntityTameEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getOwner()).orElseThrow())
        );
    }
}
