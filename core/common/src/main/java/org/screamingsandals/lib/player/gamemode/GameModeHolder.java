package org.screamingsandals.lib.player.gamemode;

import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
public interface GameModeHolder extends ComparableWrapper, RawValueHolder {

    String platformName();

    int id();

    /**
     * Use the fluent form!
     */
    @Deprecated(forRemoval = true)
    default int getId() {
        return id();
    }

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.GAME_MODE)
    boolean is(Object gameMode);

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.GAME_MODE)
    boolean is(Object... gameModes);

    @CustomAutocompletion(CustomAutocompletion.Type.GAME_MODE)
    static GameModeHolder of(Object gameMode) {
        return ofOptional(gameMode).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.GAME_MODE)
    static Optional<GameModeHolder> ofOptional(Object gameMode) {
        if (gameMode instanceof GameModeHolder) {
            return Optional.of((GameModeHolder) gameMode);
        }
        return GameModeMapping.resolve(gameMode);
    }

    static List<GameModeHolder> all() {
        return GameModeMapping.getValues();
    }
}
