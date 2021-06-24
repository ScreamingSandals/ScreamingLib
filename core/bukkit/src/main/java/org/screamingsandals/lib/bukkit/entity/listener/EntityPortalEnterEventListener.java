package org.screamingsandals.lib.bukkit.entity.listener;

import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.event.SEntityPortalEnterEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.world.LocationMapper;

public class EntityPortalEnterEventListener extends AbstractBukkitEventHandlerFactory<EntityPortalEnterEvent, SEntityPortalEnterEvent> {

    public EntityPortalEnterEventListener(Plugin plugin) {
        super(EntityPortalEnterEvent.class, SEntityPortalEnterEvent.class, plugin);
    }

    @Override
    protected SEntityPortalEnterEvent wrapEvent(EntityPortalEnterEvent event, EventPriority priority) {
        return new SEntityPortalEnterEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                LocationMapper.wrapLocation(event.getLocation())
        );
    }
}
