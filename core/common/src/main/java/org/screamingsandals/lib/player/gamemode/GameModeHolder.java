package org.screamingsandals.lib.player.gamemode;

import lombok.Data;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
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

    @CustomAutocompletion(CustomAutocompletion.Type.GAME_MODE)
    public boolean is(Object gameMode) {
        return equals(ofOptional(gameMode).orElse(null));
    }

    @CustomAutocompletion(CustomAutocompletion.Type.GAME_MODE)
    public boolean is(Object... gameModes) {
        return Arrays.stream(gameModes).anyMatch(this::is);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.GAME_MODE)
    public static GameModeHolder of(Object gameMode) {
        return ofOptional(gameMode).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.GAME_MODE)
    public static Optional<GameModeHolder> ofOptional(Object gameMode) {
        if (gameMode instanceof GameModeHolder) {
            return Optional.of((GameModeHolder) gameMode);
        }
        return GameModeMapping.resolve(gameMode);
    }

    public static List<GameModeHolder> all() {
        return GameModeMapping.getValues();
    }
}
