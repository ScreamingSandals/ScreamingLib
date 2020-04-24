package org.screamingsandals.gamecore.player;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    private final Map<Player, GamePlayer> registeredPlayers = new HashMap<>();

    public GamePlayer registerPlayer(Player player) {
        final GamePlayer gamePlayer = new GamePlayer(player);
        registeredPlayers.put(player, gamePlayer);

        return gamePlayer;
    }

    public void unregisterPlayer(Player player) {
        if (registeredPlayers.containsKey(player)) {
            final GamePlayer gamePlayer = registeredPlayers.get(player);
            gamePlayer.destroy();
        }
        registeredPlayers.remove(player);
    }

    public void unregisterPlayer(GamePlayer gamePlayer) {
        unregisterPlayer(gamePlayer.getBukkitPlayer());
    }

    public boolean isPlayerRegistered(Player player) {
        return registeredPlayers.containsKey(player);
    }
}
