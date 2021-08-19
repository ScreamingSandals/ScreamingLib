package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityToggleSwimEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityToggleSwimEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class EntityToggleSwimEventListener extends AbstractBukkitEventHandlerFactory<EntityToggleSwimEvent, SEntityToggleSwimEvent> {

    public EntityToggleSwimEventListener(Plugin plugin) {
        super(EntityToggleSwimEvent.class, SEntityToggleSwimEvent.class, plugin);
    }

    @Override
    protected SEntityToggleSwimEvent wrapEvent(EntityToggleSwimEvent event, EventPriority priority) {
        return new SEntityToggleSwimEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(event::isSwimming)
        );
    }
}
