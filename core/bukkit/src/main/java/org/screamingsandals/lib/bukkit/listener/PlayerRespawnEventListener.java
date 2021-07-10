package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerRespawnEvent;
import org.screamingsandals.lib.world.LocationMapper;

public class PlayerRespawnEventListener extends AbstractBukkitEventHandlerFactory<PlayerRespawnEvent, SPlayerRespawnEvent> {

    public PlayerRespawnEventListener(Plugin plugin) {
        super(PlayerRespawnEvent.class, SPlayerRespawnEvent.class, plugin);
    }

    @Override
    protected SPlayerRespawnEvent wrapEvent(PlayerRespawnEvent event, EventPriority priority) {
        return new SPlayerRespawnEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                LocationMapper.wrapLocation(event.getRespawnLocation())
        );
    }

    @Override
    protected void postProcess(SPlayerRespawnEvent wrappedEvent, PlayerRespawnEvent event) {
        event.setRespawnLocation(wrappedEvent.getLocation().as(Location.class));
    }
}
