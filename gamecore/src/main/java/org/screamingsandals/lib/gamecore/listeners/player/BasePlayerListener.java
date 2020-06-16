package org.screamingsandals.lib.gamecore.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.events.player.damage.SPlayerDamagedEntityEvent;
import org.screamingsandals.lib.gamecore.events.player.damage.SPlayerDamagedEvent;
import org.screamingsandals.lib.gamecore.events.player.damage.SPlayerDamagedPlayerEvent;
import org.screamingsandals.lib.gamecore.events.player.move.SPlayerMovedEvent;
import org.screamingsandals.lib.gamecore.player.GamePlayer;

public class BasePlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final GamePlayer gamePlayer = GameCore.getPlayerManager().registerPlayer(player);

        /*
        If we cant register the player, whole plugin will write shitloads of errors.
        Do we need better way for this or not?
        */
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

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final var entity = event.getEntity();

        if (!(entity instanceof Player)) {
            return;
        }

        final var registeredPlayer = GameCore.getPlayerManager().getRegisteredPlayer((Player) entity);
        if (registeredPlayer.isEmpty()) {
            return;
        }

        final var gamePlayer = registeredPlayer.get();
        final var game = gamePlayer.getActiveGame();

        if (gamePlayer.isInGame() && !GameCore.fireEvent(new SPlayerDamagedEvent(gamePlayer, game, event.getCause()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
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

            if (!GameCore.fireEvent(new SPlayerMovedEvent(gamePlayer, game, event.getFrom(), event.getTo()))) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityByEntityDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final var eAttacker = event.getDamager();
        final var entity = event.getEntity();

        if (!(eAttacker instanceof Player)) {
            return;
        }

        final var rAttacker = GameCore.getPlayerManager().getRegisteredPlayer((Player) eAttacker);
        if (rAttacker.isEmpty()) {
            return;
        }

        final var attacker = rAttacker.get();
        final var game = attacker.getActiveGame();

        if (!(entity instanceof Player)) {
            if (!GameCore.fireEvent(new SPlayerDamagedEntityEvent(attacker, entity, game, event.getCause()))) {
                event.setCancelled(true);
            }
            return;
        }

        final var registeredPlayer = GameCore.getPlayerManager().getRegisteredPlayer((Player) entity);
        if (registeredPlayer.isEmpty()) {
            return;
        }

        final var damaged = registeredPlayer.get();

        if (attacker.isInGame() && !GameCore.fireEvent(new SPlayerDamagedPlayerEvent(attacker, damaged, game, event.getCause()))) {
            event.setCancelled(true);
        }
    }
}
