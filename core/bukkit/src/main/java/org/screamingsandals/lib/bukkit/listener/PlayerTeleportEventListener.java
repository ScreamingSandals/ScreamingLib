package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.player.SPlayerPortalEvent;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerTeleportEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.world.LocationMapper;

public class PlayerTeleportEventListener extends AbstractBukkitEventHandlerFactory<PlayerTeleportEvent, SPlayerTeleportEvent> {

    public PlayerTeleportEventListener(Plugin plugin) {
        super(PlayerTeleportEvent.class, SPlayerTeleportEvent.class, plugin);
    }

    @Override
    protected SPlayerTeleportEvent wrapEvent(PlayerTeleportEvent event, EventPriority priority) {
        if (event instanceof PlayerPortalEvent) {
            return new SPlayerPortalEvent(
                    ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                    ObjectLink.of(
                            () -> LocationMapper.wrapLocation(event.getFrom()),
                            locationHolder -> event.setFrom(locationHolder.as(Location.class))
                    ),
                    ObjectLink.of(
                            () -> LocationMapper.wrapLocation(event.getTo()),
                            locationHolder -> event.setTo(locationHolder.as(Location.class))
                    ),
                    ImmutableObjectLink.of(() -> SPlayerTeleportEvent.TeleportCause.getByName(event.getCause().name())
                            .orElse(SPlayerTeleportEvent.TeleportCause.UNKNOWN)),
                    ObjectLink.of(((PlayerPortalEvent) event)::getSearchRadius, ((PlayerPortalEvent) event)::setSearchRadius),
                    ObjectLink.of(((PlayerPortalEvent) event)::getCanCreatePortal, ((PlayerPortalEvent) event)::setCanCreatePortal),
                    ObjectLink.of(((PlayerPortalEvent) event)::getCreationRadius, ((PlayerPortalEvent) event)::setCreationRadius)
            );
        }

        return new SPlayerTeleportEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                ObjectLink.of(
                        () -> LocationMapper.wrapLocation(event.getFrom()),
                        locationHolder -> event.setFrom(locationHolder.as(Location.class))
                ),
                ObjectLink.of(
                        () -> LocationMapper.wrapLocation(event.getTo()),
                        locationHolder -> event.setTo(locationHolder.as(Location.class))
                ),
                ImmutableObjectLink.of(() -> SPlayerTeleportEvent.TeleportCause.getByName(event.getCause().name())
                        .orElse(SPlayerTeleportEvent.TeleportCause.UNKNOWN))
        );
    }
}
