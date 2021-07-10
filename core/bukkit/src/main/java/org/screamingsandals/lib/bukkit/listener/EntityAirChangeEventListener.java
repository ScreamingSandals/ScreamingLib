package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityAirChangeEvent;
import org.screamingsandals.lib.event.EventPriority;

public class EntityAirChangeEventListener extends AbstractBukkitEventHandlerFactory<EntityAirChangeEvent, SEntityAirChangeEvent> {

    public EntityAirChangeEventListener(Plugin plugin) {
        super(EntityAirChangeEvent.class, SEntityAirChangeEvent.class, plugin);
    }

    @Override
    protected SEntityAirChangeEvent wrapEvent(EntityAirChangeEvent event, EventPriority priority) {
        return new SEntityAirChangeEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                event.getAmount()
        );
    }

    @Override
    protected void postProcess(SEntityAirChangeEvent wrappedEvent, EntityAirChangeEvent event) {
        event.setAmount(wrappedEvent.getAmount());
    }
}
