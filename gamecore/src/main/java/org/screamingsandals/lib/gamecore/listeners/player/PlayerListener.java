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

        final var gamePlayer = GameCore.getPlayerManager().getRegisteredPlayer(event.getPlayer());

        if (gamePlayer.isInGame()) {
            final var game = gamePlayer.getActiveGame();
            final var gameState = game.getActiveState();
            final var gameWorld = game.getGameWorld();
            final var lobbyWorld = game.getLobbyWorld();

            if (gameState == GameState.WAITING || gameState == GameState.PRE_GAME_COUNTDOWN) {
                final var to = event.getTo();
                if (to.getBlock().getType().isAir()) {
                    return;
                }

                if (!GameUtils.isInGameBorder(to, lobbyWorld.getBorder1().getLocation(), lobbyWorld.getBorder2().getLocation())) {
                    event.setCancelled(true);
                }
                return;
            }

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
