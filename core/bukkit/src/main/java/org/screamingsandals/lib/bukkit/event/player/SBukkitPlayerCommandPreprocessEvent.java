package org.screamingsandals.lib.bukkit.event.player;

import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.player.SPlayerCommandPreprocessEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@Data
public class SBukkitPlayerCommandPreprocessEvent implements SPlayerCommandPreprocessEvent, BukkitCancellable {
    private final PlayerCommandPreprocessEvent event;

    @Override
    public PlayerWrapper getPlayer() {
        return new BukkitEntityPlayer(event.getPlayer());
    }
    @Override
    public void setPlayer(PlayerWrapper player) {
        event.setPlayer(player.as(Player.class));
    }

    @Override
    public String getCommand() {
        return event.getMessage();
    }

    @Override
    public void setCommand(String command) {
        event.setMessage(command);
    }
}
