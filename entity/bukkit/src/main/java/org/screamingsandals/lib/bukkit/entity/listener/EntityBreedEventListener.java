package org.screamingsandals.lib.bukkit.entity.listener;

import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.event.SEntityBreedEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;

public class EntityBreedEventListener extends AbstractBukkitEventHandlerFactory<EntityBreedEvent, SEntityBreedEvent> {

    public EntityBreedEventListener(Plugin plugin) {
        super(EntityBreedEvent.class, SEntityBreedEvent.class, plugin);
    }

    @Override
    protected SEntityBreedEvent wrapEvent(EntityBreedEvent event, EventPriority priority) {
        return new SEntityBreedEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                EntityMapper.wrapEntity(event.getMother()).orElseThrow(),
                EntityMapper.wrapEntity(event.getFather()).orElseThrow(),
                EntityMapper.wrapEntity(event.getBreeder()).orElseThrow(),
                ItemFactory.build(event.getBredWith()).orElseThrow(),
                event.getExperience()
        );
    }

    @Override
    protected void postProcess(SEntityBreedEvent wrappedEvent, EntityBreedEvent event) {
        event.setExperience(wrappedEvent.getExperience());
    }
}
