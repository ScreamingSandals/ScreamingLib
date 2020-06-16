package org.screamingsandals.lib.gamecore.listeners.player.game;

import static org.screamingsandals.lib.gamecore.language.GameLanguage.mpr;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.screamingsandals.lib.gamecore.events.player.damage.SPlayerDamagedEvent;
import org.screamingsandals.lib.gamecore.events.player.damage.SPlayerDamagedPlayerEvent;

public class LobbyListener implements Listener {

    @EventHandler
    public void onSDamage(SPlayerDamagedEvent event) {
        final var gamePlayer = event.getDamaged();
        final var cause = event.getDamageCause();
        final var game = event.getGameFrame();

        if (!game.isWaiting() || !game.isStarting()) {
            return;
        }

        if (cause == EntityDamageEvent.DamageCause.VOID) {
            gamePlayer.teleport(game.getLobbyWorld().getSpawn())
                    .then(() -> mpr("").send(gamePlayer))
                    .done();
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onSDamage(SPlayerDamagedPlayerEvent event) {
        final var game = event.getGameFrame();

        if (!game.isWaiting() || !game.isStarting()) {
            return;
        }
        event.setCancelled(true);
    }


}
