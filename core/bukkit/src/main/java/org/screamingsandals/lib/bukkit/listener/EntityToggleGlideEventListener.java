package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityToggleGlideEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class EntityToggleGlideEventListener extends AbstractBukkitEventHandlerFactory<EntityToggleGlideEvent, SEntityToggleGlideEvent> {

    public EntityToggleGlideEventListener(Plugin plugin) {
        super(EntityToggleGlideEvent.class, SEntityToggleGlideEvent.class, plugin);
    }

    @Override
    protected SEntityToggleGlideEvent wrapEvent(EntityToggleGlideEvent event, EventPriority priority) {
        return new SEntityToggleGlideEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(event::isGliding)
        );
    }
}
