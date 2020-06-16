package org.screamingsandals.lib.gamecore.listeners.player.game;

import static org.screamingsandals.lib.gamecore.language.GameLanguage.mpr;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.screamingsandals.lib.gamecore.events.player.move.SPlayerMovedEvent;
import org.screamingsandals.lib.gamecore.utils.GameUtils;

public class InGameListener implements Listener {

    @EventHandler
    public void onSMove(SPlayerMovedEvent event) {
        final var gamePlayer = event.getGamePlayer();
        final var game = event.getGameFrame();
        final var world = game.getGameWorld();
        final var locationTo = event.getTo();

        if (game.isGameRunning() && !gamePlayer.isSpectator()) {
            if (!GameUtils.isInGameBorder(locationTo, world.getBorder1().getLocation(), world.getBorder2().getLocation())) {
                gamePlayer.getPlayer().damage(5);
            }
            return;
        }

        if (game.isGameRunning() && gamePlayer.isSpectator()) {
            if (!GameUtils.isInGameBorder(locationTo, world.getBorder1().getLocation(), world.getBorder2().getLocation())) {
                event.setCancelled(true);

                gamePlayer.teleport(world.getSpectatorSpawn())
                        //TODO - mpr
                        .then(() -> mpr("").send(gamePlayer))
                        .done();
            }
        }
    }
}
