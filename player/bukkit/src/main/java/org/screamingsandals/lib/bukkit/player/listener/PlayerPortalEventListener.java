package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerPortalEvent;
import org.screamingsandals.lib.player.event.SPlayerTeleportEvent;
import org.screamingsandals.lib.world.LocationMapper;

public class PlayerPortalEventListener extends AbstractBukkitEventHandlerFactory<PlayerPortalEvent, SPlayerPortalEvent> {

    public PlayerPortalEventListener(Plugin plugin) {
        super(PlayerPortalEvent.class, SPlayerPortalEvent.class, plugin);
    }

    @Override
    protected SPlayerPortalEvent wrapEvent(PlayerPortalEvent event, EventPriority priority) {
        return new SPlayerPortalEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                LocationMapper.wrapLocation(event.getFrom()),
                LocationMapper.wrapLocation(event.getTo()),
                SPlayerTeleportEvent.TeleportCause.valueOf(event.getCause().name().toUpperCase())
        );
    }

    @Override
    protected void postProcess(SPlayerPortalEvent wrappedEvent, PlayerPortalEvent event) {
        event.setCanCreatePortal(wrappedEvent.isCanCreatePortal());
        event.setCreationRadius(wrappedEvent.getCreationRadius());
        event.setSearchRadius(wrappedEvent.getGetSearchRadius());
        event.setFrom(wrappedEvent.getCurrentLocation().as(Location.class));
        event.setTo(wrappedEvent.getNewLocation().as(Location.class));
    }
}
