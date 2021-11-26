package org.screamingsandals.lib.bukkit.event.player;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.player.SPlayerRespawnEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

@Data
public class SBukkitPlayerRespawnEvent implements SPlayerRespawnEvent, BukkitCancellable {
    private final PlayerRespawnEvent event;

    // Internal cache
    private PlayerWrapper player;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public LocationHolder getLocation() {
        return LocationMapper.wrapLocation(event.getRespawnLocation());
    }

    @Override
    public void setLocation(LocationHolder location) {
        event.setRespawnLocation(location.as(Location.class));
    }
}
