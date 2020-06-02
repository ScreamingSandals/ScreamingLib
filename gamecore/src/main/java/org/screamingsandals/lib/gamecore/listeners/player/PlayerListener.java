package org.screamingsandals.lib.gamecore.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.core.GameState;
import org.screamingsandals.lib.gamecore.player.GamePlayer;
import org.screamingsandals.lib.gamecore.utils.GameUtils;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final GamePlayer gamePlayer = GameCore.getPlayerManager().registerPlayer(player);

        if (gamePlayer == null) {
            player.kickPlayer("YAAHOOO, can't dooo. Maintenance maybe?"); //TODO - language
        }
    }

    @EventHandler
    public void onPlayerLeft(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        GameCore.getPlayerManager().unregisterPlayer(player);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        final Player player = event.getPlayer();

        if (!GameCore.getPlayerManager().isPlayerRegistered(player)) {
            return;
        }

        GameCore.getPlayerManager().unregisterPlayer(player);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final var entity = event.getRightClicked();

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final var gamePlayerRegistration = GameCore.getPlayerManager().getRegisteredPlayer(event.getPlayer());
        if (gamePlayerRegistration.isEmpty()) {
            return;
        }

        final var gamePlayer = gamePlayerRegistration.get();

        if (gamePlayer.isInGame()) {
            final var game = gamePlayer.getActiveGame();
            final var gameState = game.getActiveState();
            final var gameWorld = game.getGameWorld();

            if (gameState == GameState.IN_GAME && !gamePlayer.isSpectator()) {
                if (!GameUtils.isInGameBorder(event.getTo(), gameWorld.getBorder1().getLocation(), gameWorld.getBorder2().getLocation())) {
                    gamePlayer.getPlayer().damage(5);
                }
                return;
            }

            if (gamePlayer.isSpectator() && (gameState == GameState.IN_GAME
                    || gameState == GameState.AFTER_GAME_COUNTDOWN
                    || gameState == GameState.DEATHMATCH)) {
                if (!GameUtils.isInGameBorder(event.getTo(), gameWorld.getBorder1().getLocation(), gameWorld.getBorder2().getLocation())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
