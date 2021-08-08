package org.screamingsandals.lib.player.gamemode;

import lombok.Data;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.Arrays;
import java.util.Optional;

@Data
public class GameModeHolder implements Wrapper {
    private final String platformName;

    @Override
    public <T> T as(Class<T> type) {
        return GameModeMapping.convertGameModeHolder(this, type);
    }

    public int getId() {
        return GameModeMapping.getId(this);
    }

    public boolean is(Object gameMode) {
        return equals(GameModeMapping.resolve(gameMode).orElse(null));
    }

    public boolean is(Object... gameModes) {
        return Arrays.stream(gameModes).anyMatch(this::is);
    }

    public static GameModeHolder of(Object gameMode) {
        return ofOptional(gameMode).orElseThrow();
    }

    public static Optional<GameModeHolder> ofOptional(Object gameMode) {
        if (gameMode instanceof GameModeHolder) {
            return Optional.of((GameModeHolder) gameMode);
        }
        return GameModeMapping.resolve(gameMode);
    }
}
