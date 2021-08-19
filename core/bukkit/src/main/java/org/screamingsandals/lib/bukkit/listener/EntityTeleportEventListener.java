package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.Location;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityPortalEvent;
import org.screamingsandals.lib.event.entity.SEntityTeleportEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.world.LocationMapper;

public class EntityTeleportEventListener extends AbstractBukkitEventHandlerFactory<EntityTeleportEvent, SEntityTeleportEvent> {

    public EntityTeleportEventListener(Plugin plugin) {
        super(EntityTeleportEvent.class, SEntityTeleportEvent.class, plugin);
    }

    @Override
    protected SEntityTeleportEvent wrapEvent(EntityTeleportEvent event, EventPriority priority) {
        if (event instanceof EntityPortalEvent) {
            return new SEntityPortalEvent(
                    ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                    ObjectLink.of(() -> LocationMapper.wrapLocation(event.getFrom()), locationHolder -> event.setFrom(locationHolder.as(Location.class))),
                    ObjectLink.of(() -> LocationMapper.wrapLocation(event.getTo()), locationHolder -> event.setTo(locationHolder.as(Location.class))),
                    ObjectLink.of(((EntityPortalEvent) event)::getSearchRadius, ((EntityPortalEvent) event)::setSearchRadius)
            );
        }

        return new SEntityTeleportEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ObjectLink.of(() -> LocationMapper.wrapLocation(event.getFrom()), locationHolder -> event.setFrom(locationHolder.as(Location.class))),
                ObjectLink.of(() -> LocationMapper.wrapLocation(event.getTo()), locationHolder -> event.setTo(locationHolder.as(Location.class)))
        );
    }
}
