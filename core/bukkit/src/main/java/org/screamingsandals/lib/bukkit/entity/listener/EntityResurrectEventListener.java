package org.screamingsandals.lib.bukkit.entity.listener;

import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.event.SEntityResurrectEvent;
import org.screamingsandals.lib.event.EventPriority;

public class EntityResurrectEventListener extends AbstractBukkitEventHandlerFactory<EntityResurrectEvent, SEntityResurrectEvent> {

    public EntityResurrectEventListener(Plugin plugin) {
        super(EntityResurrectEvent.class, SEntityResurrectEvent.class, plugin);
    }

    @Override
    protected SEntityResurrectEvent wrapEvent(EntityResurrectEvent event, EventPriority priority) {
        return new SEntityResurrectEvent(EntityMapper.wrapEntity(event.getEntity()).orElseThrow());
    }
}
