package org.screamingsandals.lib.bukkit.entity.listener;

import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.event.SEntityDeathEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;

import java.util.Optional;
import java.util.stream.Collectors;

public class EntityDeathEventListener extends AbstractBukkitEventHandlerFactory<EntityDeathEvent, SEntityDeathEvent> {

    public EntityDeathEventListener(Plugin plugin) {
        super(EntityDeathEvent.class, SEntityDeathEvent.class, plugin);
    }

    @Override
    protected SEntityDeathEvent wrapEvent(EntityDeathEvent event, EventPriority priority) {
        return new SEntityDeathEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                event.getDrops()
                        .stream()
                        .map(ItemFactory::build)
                        .map(Optional::orElseThrow)
                        .collect(Collectors.toList()),
                event.getDroppedExp()
        );
    }

    @Override
    protected void postProcess(SEntityDeathEvent wrappedEvent, EntityDeathEvent event) {
        event.getDrops().clear();
        wrappedEvent
                .getDrops()
                .stream()
                .map(item -> item.as(ItemStack.class))
                .forEach(event.getDrops()::add);
    }
}
