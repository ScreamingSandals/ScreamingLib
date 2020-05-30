package org.screamingsandals.lib.gamecore.player;

import org.bukkit.entity.Player;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.events.player.SPlayerPreRegisterEvent;
import org.screamingsandals.lib.gamecore.events.player.SPlayerRegisteredEvent;
import org.screamingsandals.lib.gamecore.events.player.SPlayerUnregisteredEvent;

import java.util.*;

public class PlayerManager {
    private final Map<Player, GamePlayer> registeredPlayers = new HashMap<>();

    public GamePlayer registerPlayer(Player player) {
        if (!GameCore.fireEvent(new SPlayerPreRegisterEvent(player))) {
            return null;
        }

        final GamePlayer gamePlayer = new GamePlayer(player);
        registeredPlayers.put(player, gamePlayer);

        GameCore.fireEvent(new SPlayerRegisteredEvent(gamePlayer));
        return gamePlayer;
    }

    public void unregisterPlayer(Player player) {
        if (registeredPlayers.containsKey(player)) {
            registeredPlayers.get(player).destroy();
        }
        registeredPlayers.remove(player);

        GameCore.fireEvent(new SPlayerUnregisteredEvent(player.getUniqueId()));
    }

    public void unregisterPlayer(GamePlayer gamePlayer) {
        unregisterPlayer(gamePlayer.getPlayer());
    }

    public boolean isPlayerRegistered(Player player) {
        return registeredPlayers.containsKey(player);
    }

    public boolean isPlayerRegistered(UUID uuid) {
        for (var player : registeredPlayers.values()) {
            if (player.getUuid().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    public Optional<GamePlayer> getRegisteredPlayer(Player player) {
        final var gamePlayer = registeredPlayers.get(player);
        return Optional.ofNullable(gamePlayer);
    }

    public Optional<GamePlayer> getRegisteredPlayer(UUID uuid) {
        for (var player : registeredPlayers.values()) {
            if (player.getUuid().equals(uuid)) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }

    public Collection<GamePlayer> getRegisteredPlayers() {
        return registeredPlayers.values();
    }
}
