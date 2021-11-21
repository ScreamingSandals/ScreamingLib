package org.screamingsandals.lib.bukkit.event.player;

import lombok.Data;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.event.player.SPlayerCommandSendEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.Collection;

@Data
public class SBukkitPlayerCommandSendEvent implements SPlayerCommandSendEvent {
    private final PlayerCommandSendEvent event;

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
    public Collection<String> getCommands() {
        return event.getCommands();
    }
}
