package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityBreedEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class EntityBreedEventListener extends AbstractBukkitEventHandlerFactory<EntityBreedEvent, SEntityBreedEvent> {

    public EntityBreedEventListener(Plugin plugin) {
        super(EntityBreedEvent.class, SEntityBreedEvent.class, plugin);
    }

    @Override
    protected SEntityBreedEvent wrapEvent(EntityBreedEvent event, EventPriority priority) {
        return new SEntityBreedEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getMother()).orElseThrow()),
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getFather()).orElseThrow()),
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getBreeder()).orElse(null)),
                ImmutableObjectLink.of(() -> ItemFactory.build(event.getBredWith()).orElse(null)),
                ObjectLink.of(event::getExperience, event::setExperience)
        );
    }
}
