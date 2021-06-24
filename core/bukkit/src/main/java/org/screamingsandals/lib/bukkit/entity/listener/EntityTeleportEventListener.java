package org.screamingsandals.lib.bukkit.entity.listener;

import org.bukkit.Location;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.event.SEntityPortalEvent;
import org.screamingsandals.lib.entity.event.SEntityTeleportEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.world.LocationMapper;

public class EntityTeleportEventListener extends AbstractBukkitEventHandlerFactory<EntityTeleportEvent, SEntityTeleportEvent> {

    public EntityTeleportEventListener(Plugin plugin) {
        super(EntityTeleportEvent.class, SEntityTeleportEvent.class, plugin);
    }

    @Override
    protected SEntityTeleportEvent wrapEvent(EntityTeleportEvent event, EventPriority priority) {
        if (event instanceof EntityPortalEvent) {
            return new SEntityPortalEvent(
                    EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                    LocationMapper.wrapLocation(event.getFrom()),
                    LocationMapper.wrapLocation(event.getTo())
            );
        }

        return new SEntityPortalEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                LocationMapper.wrapLocation(event.getFrom()),
                LocationMapper.wrapLocation(event.getTo())
        );
    }

    @Override
    protected void postProcess(SEntityTeleportEvent wrappedEvent, EntityTeleportEvent event) {
        event.setFrom(wrappedEvent.getFrom().as(Location.class));
        event.setTo(wrappedEvent.getTo().as(Location.class));
    }
}
