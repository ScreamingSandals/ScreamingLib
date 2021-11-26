package org.screamingsandals.lib.bukkit.event.player;

import lombok.Data;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.world.BukkitWorldHolder;
import org.screamingsandals.lib.event.player.SPlayerWorldChangeEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.WorldHolder;

@Data
public class SBukkitPlayerWorldChangeEvent implements SPlayerWorldChangeEvent {
    private final PlayerChangedWorldEvent event;

    // Internal cache
    private PlayerWrapper player;
    private WorldHolder from;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public WorldHolder getFrom() {
        if (from == null) {
            from = new BukkitWorldHolder(event.getFrom());
        }
        return from;
    }
}
