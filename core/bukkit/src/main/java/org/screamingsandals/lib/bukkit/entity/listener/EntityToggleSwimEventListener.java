package org.screamingsandals.lib.bukkit.entity.listener;

import org.bukkit.event.entity.EntityToggleSwimEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.event.SEntityToggleSwimEvent;
import org.screamingsandals.lib.event.EventPriority;

public class EntityToggleSwimEventListener extends AbstractBukkitEventHandlerFactory<EntityToggleSwimEvent, SEntityToggleSwimEvent> {

    public EntityToggleSwimEventListener(Plugin plugin) {
        super(EntityToggleSwimEvent.class, SEntityToggleSwimEvent.class, plugin);
    }

    @Override
    protected SEntityToggleSwimEvent wrapEvent(EntityToggleSwimEvent event, EventPriority priority) {
        return new SEntityToggleSwimEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                event.isSwimming()
        );
    }
}
