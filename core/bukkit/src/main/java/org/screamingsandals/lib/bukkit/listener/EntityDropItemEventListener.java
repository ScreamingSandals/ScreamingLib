package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityDropItemEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;

public class EntityDropItemEventListener extends AbstractBukkitEventHandlerFactory<EntityDropItemEvent, SEntityDropItemEvent> {

    public EntityDropItemEventListener(Plugin plugin) {
        super(EntityDropItemEvent.class, SEntityDropItemEvent.class, plugin);
    }

    @Override
    protected SEntityDropItemEvent wrapEvent(EntityDropItemEvent event, EventPriority priority) {
        return new SEntityDropItemEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                ItemFactory.build(event.getItemDrop()).orElseThrow()
        );
    }
}
