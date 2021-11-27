package org.screamingsandals.lib.bukkit.event.player;

import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.entity.SBukkitEntityUnleashEvent;
import org.screamingsandals.lib.event.player.SPlayerUnleashEntityEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

public class SBukkitPlayerUnleashEntityEvent extends SBukkitEntityUnleashEvent implements SPlayerUnleashEntityEvent {
    public SBukkitPlayerUnleashEntityEvent(PlayerUnleashEntityEvent event) {
        super(event);
    }

    // Internal cache
    private PlayerWrapper player;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(((PlayerUnleashEntityEvent) getEvent()).getPlayer());
        }
        return player;
    }
}
