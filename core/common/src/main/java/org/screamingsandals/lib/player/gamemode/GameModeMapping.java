package org.screamingsandals.lib.player.gamemode;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
public abstract class GameModeMapping extends AbstractTypeMapper<GameModeHolder> {
    private static GameModeMapping gameModeMapping;

    protected final BidirectionalConverter<GameModeHolder> gameModeConverter = BidirectionalConverter.<GameModeHolder>build()
            .registerP2W(GameModeHolder.class, g -> g);

    @ApiStatus.Internal
    public GameModeMapping() {
        if (gameModeMapping != null) {
            throw new UnsupportedOperationException("GameModeMapping is already initialized!");
        }
        gameModeMapping = this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.GAME_MODE)
    @OfMethodAlternative(value = GameModeHolder.class, methodName = "ofOptional")
    public static Optional<GameModeHolder> resolve(Object gameMode) {
        if (gameModeMapping == null) {
            throw new UnsupportedOperationException("GameModeMapping is not initialized yet.");
        }

        if (gameMode == null) {
            return Optional.empty();
        }

        return gameModeMapping.gameModeConverter.convertOptional(gameMode).or(() -> gameModeMapping.resolveFromMapping(gameMode));
    }

    @OfMethodAlternative(value = GameModeHolder.class, methodName = "all")
    public static List<GameModeHolder> getValues() {
        if (gameModeMapping == null) {
            throw new UnsupportedOperationException("GameModeMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(gameModeMapping.values);
    }

    public static <T> T convertGameModeHolder(GameModeHolder holder, Class<T> newType) {
        if (gameModeMapping == null) {
            throw new UnsupportedOperationException("GameModeMapping is not initialized yet.");
        }
        return gameModeMapping.gameModeConverter.convert(holder, newType);
    }

    public static int getId(GameModeHolder holder) {
        if (gameModeMapping == null) {
            throw new UnsupportedOperationException("GameModeMapping is not initialized yet.");
        }
        return gameModeMapping.getId0(holder);
    }

    protected abstract int getId0(GameModeHolder holder);
}
