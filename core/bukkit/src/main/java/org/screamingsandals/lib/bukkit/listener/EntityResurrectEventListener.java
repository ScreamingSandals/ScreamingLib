package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityResurrectEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class EntityResurrectEventListener extends AbstractBukkitEventHandlerFactory<EntityResurrectEvent, SEntityResurrectEvent> {

    public EntityResurrectEventListener(Plugin plugin) {
        super(EntityResurrectEvent.class, SEntityResurrectEvent.class, plugin);
    }

    @Override
    protected SEntityResurrectEvent wrapEvent(EntityResurrectEvent event, EventPriority priority) {
        return new SEntityResurrectEvent(ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()));
    }
}
