package org.screamingsandals.lib.scoreboard;

import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@AbstractService
public abstract class ScoreboardManager {
    private static ScoreboardManager manager;
    protected final Map<UUID, Scoreboard> activeScoreboards = new HashMap<>();

    @Deprecated //INTERNAL USE ONLY!
    public static void init(Supplier<ScoreboardManager> supplier) {
        if (manager != null) {
            throw new UnsupportedOperationException("ScoreboardManager is already initialized");
        }
        manager = supplier.get();
    }

    protected ScoreboardManager(Controllable controllable) {
        controllable.disable(this::destroy);
    }

    public static Map<UUID, Scoreboard> getActiveScoreboards() {
        if (manager == null) {
            throw new UnsupportedOperationException("ScoreboardManager is not initialized yet!");
        }

        return Map.copyOf(manager.activeScoreboards);
    }

    public static Optional<Scoreboard> getScoreboard(UUID uuid) {
        if (manager == null) {
            throw new UnsupportedOperationException("ScoreboardManager is not initialized yet!");
        }

        return Optional.ofNullable(manager.activeScoreboards.get(uuid));
    }

    public static void addScoreboard(Scoreboard scoreboard) {
        if (manager == null) {
            throw new UnsupportedOperationException("ScoreboardManager is not initialized yet!");
        }

        manager.activeScoreboards.put(scoreboard.getUuid(), scoreboard);
    }

    public static void removeScoreboard(UUID uuid) {
        getScoreboard(uuid).ifPresent(ScoreboardManager::removeScoreboard);
    }

    public static void removeScoreboard(Scoreboard scoreboard) {
        if (manager == null) {
            throw new UnsupportedOperationException("ScoreboardManager is not initialized yet!");
        }

        manager.activeScoreboards.remove(scoreboard.getUuid());
    }

    public static Scoreboard scoreboard() {
        return scoreboard(UUID.randomUUID());
    }

    public static Scoreboard scoreboard(UUID uuid) {
        if (manager == null) {
            throw new UnsupportedOperationException("ScoreboardManager is not initialized yet!");
        }

        final var scoreboard = manager.scoreboard0(uuid);
        addScoreboard(scoreboard);
        return scoreboard;
    }

    protected abstract Scoreboard scoreboard0(UUID uuid);

    protected void destroy() {
        Map.copyOf(getActiveScoreboards())
                .values()
                .forEach(Scoreboard::destroy);
        manager.activeScoreboards.clear();
    }
}
