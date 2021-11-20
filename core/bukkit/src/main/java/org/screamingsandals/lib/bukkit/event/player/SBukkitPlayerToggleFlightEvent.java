package org.screamingsandals.lib.bukkit.event.player;

import lombok.Data;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.player.SPlayerToggleFlightEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@Data
public class SBukkitPlayerToggleFlightEvent implements SPlayerToggleFlightEvent, BukkitCancellable {
    private final PlayerToggleFlightEvent event;

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
    public boolean isFlying() {
        return event.isFlying();
    }
}
