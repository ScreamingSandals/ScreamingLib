package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerTeleportEvent;
import org.screamingsandals.lib.world.LocationMapper;

public class PlayerTeleportEventListener extends AbstractBukkitEventHandlerFactory<PlayerTeleportEvent, SPlayerTeleportEvent> {

    public PlayerTeleportEventListener(Plugin plugin) {
        super(PlayerTeleportEvent.class, SPlayerTeleportEvent.class, plugin);
    }

    @Override
    protected SPlayerTeleportEvent wrapEvent(PlayerTeleportEvent event, EventPriority priority) {
        return new SPlayerTeleportEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                LocationMapper.wrapLocation(event.getFrom()),
                LocationMapper.wrapLocation(event.getTo()),
                SPlayerTeleportEvent.TeleportCause.getByName(event.getCause().name())
                        .orElse(SPlayerTeleportEvent.TeleportCause.UNKNOWN)
        );
    }

    @Override
    protected void postProcess(SPlayerTeleportEvent wrappedEvent, PlayerTeleportEvent event) {
        event.setFrom(wrappedEvent.getCurrentLocation().as(Location.class));
        event.setTo(wrappedEvent.getNewLocation().as(Location.class));
    }
}
