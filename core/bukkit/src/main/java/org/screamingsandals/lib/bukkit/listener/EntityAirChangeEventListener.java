package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityAirChangeEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class EntityAirChangeEventListener extends AbstractBukkitEventHandlerFactory<EntityAirChangeEvent, SEntityAirChangeEvent> {

    public EntityAirChangeEventListener(Plugin plugin) {
        super(EntityAirChangeEvent.class, SEntityAirChangeEvent.class, plugin);
    }

    @Override
    protected SEntityAirChangeEvent wrapEvent(EntityAirChangeEvent event, EventPriority priority) {
        return new SEntityAirChangeEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ObjectLink.of(event::getAmount, event::setAmount)
        );
    }
}
