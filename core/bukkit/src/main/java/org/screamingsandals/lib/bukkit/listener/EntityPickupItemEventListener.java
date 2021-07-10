package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityPickupItemEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;

public class EntityPickupItemEventListener extends AbstractBukkitEventHandlerFactory<EntityPickupItemEvent, SEntityPickupItemEvent> {

    public EntityPickupItemEventListener(Plugin plugin) {
        super(EntityPickupItemEvent.class, SEntityPickupItemEvent.class, plugin);
    }

    @Override
    protected SEntityPickupItemEvent wrapEvent(EntityPickupItemEvent event, EventPriority priority) {
        return new SEntityPickupItemEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                ItemFactory.build(event.getItem()).orElseThrow(),
                event.getRemaining()
        );
    }
}
