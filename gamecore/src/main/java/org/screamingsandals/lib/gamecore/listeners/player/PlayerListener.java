package org.screamingsandals.lib.gamecore.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.player.GamePlayer;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final GamePlayer gamePlayer = GameCore.getPlayerManager().registerPlayer(player);

        if (gamePlayer == null) {
            player.kickPlayer("YAAHOOO, can't dooo. Maintenance maybe?");
        }
    }

    @EventHandler
    public void onPlayerLeft(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        GameCore.getPlayerManager().unregisterPlayer(player);
    }
}
