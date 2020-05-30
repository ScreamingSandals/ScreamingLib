package org.screamingsandals.lib.gamecore.visuals.scoreboards;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.core.GameState;
import org.screamingsandals.lib.gamecore.player.GamePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
@ToString(exclude = "gameFrame")
public class ScoreboardManager {
    private final GameFrame gameFrame;
    private final Map<UUID, GameScoreboard> activeScoreboards = new HashMap<>();
    private final Multimap<UUID, GameScoreboard> savedScoreboards = ArrayListMultimap.create();

    public void destroy() {
        hideAll();

        activeScoreboards.clear();;
        savedScoreboards.clear();
    }

    public void hideAll() {
        getActiveScoreboards().keySet().forEach(uuid -> {
            var gamePlayerRegistration = GameCore.getPlayerManager().getRegisteredPlayer(uuid);
            if (gamePlayerRegistration.isEmpty()) {
                return;
            }

            final var gamePlayer = gamePlayerRegistration.get();

            gamePlayer.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        });
    }

    public void show(UUID uuid, GameScoreboard scoreboard) {
        var gamePlayerRegistration = GameCore.getPlayerManager().getRegisteredPlayer(uuid);
        if (gamePlayerRegistration.isEmpty()) {
            return;
        }

        final var gamePlayer = gamePlayerRegistration.get();

        activeScoreboards.remove(uuid);
        gamePlayer.getPlayer().setScoreboard(scoreboard.getBukkitScoreboard());
        activeScoreboards.put(uuid, scoreboard);
    }

    public void show(GamePlayer gamePlayer, GameScoreboard gameScoreboard) {
        show(gamePlayer.getUuid(), gameScoreboard);
    }

    public void show(GamePlayer gamePlayer, GameState gameState) {
        final var uuid = gamePlayer.getUuid();
        final var scoreboard = getSavedScoreboard(uuid, gameState.getName());

        if (scoreboard.isEmpty()) {
            return;
        }

        show(uuid, scoreboard.get());
    }

    public void hideScoreboard(UUID uuid) {
        var gamePlayerRegistration = GameCore.getPlayerManager().getRegisteredPlayer(uuid);
        if (gamePlayerRegistration.isEmpty()) {
            return;
        }

        final var gamePlayer = gamePlayerRegistration.get();

        gamePlayer.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        activeScoreboards.remove(uuid);
    }

    public void save(UUID uuid, GameScoreboard gameScoreboard) {
        savedScoreboards.put(uuid, gameScoreboard);
    }

    public void save(GamePlayer gamePlayer, GameScoreboard gameScoreboard) {
        save(gamePlayer.getUuid(), gameScoreboard);
    }

    public void removeAll(GamePlayer gamePlayer) {
        removeAll(gamePlayer.getUuid());
    }

    public void removeAll(UUID uuid) {
        savedScoreboards.removeAll(uuid);
    }

    public Optional<GameScoreboard> getSavedScoreboard(GamePlayer gamePlayer, String identifier) {
        return getSavedScoreboard(gamePlayer.getUuid(), identifier);
    }

    public Optional<GameScoreboard> getSavedScoreboard(UUID uuid, String identifier) {
        for (var scoreboard : savedScoreboards.get(uuid)) {
            if (scoreboard.getIdentifier().equals(identifier)) {
                return Optional.of(scoreboard);
            }
        }
        return Optional.empty();
    }
}
