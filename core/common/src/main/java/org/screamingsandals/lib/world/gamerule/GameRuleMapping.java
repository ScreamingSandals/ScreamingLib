package org.screamingsandals.lib.world.gamerule;

import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;

import java.util.Optional;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
public abstract class GameRuleMapping extends AbstractTypeMapper<GameRuleHolder> {
    private static GameRuleMapping gameRuleMapping;

    protected final BidirectionalConverter<GameRuleHolder> gameRuleConverter = BidirectionalConverter.<GameRuleHolder>build()
            .registerP2W(GameRuleHolder.class, d -> d);

    protected GameRuleMapping() {
        if (gameRuleMapping != null) {
            throw new UnsupportedOperationException("GameRuleMapping is already initialized!");
        }
        gameRuleMapping = this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.GAME_RULE)
    @OfMethodAlternative(value = GameRuleHolder.class, methodName = "ofOptional")
    public static Optional<GameRuleHolder> resolve(Object gameRule) {
        if (gameRuleMapping == null) {
            throw new UnsupportedOperationException("GameRuleMapping is not initialized yet.");
        }

        if (gameRule == null) {
            return Optional.empty();
        }

        return gameRuleMapping.gameRuleConverter.convertOptional(gameRule).or(() -> gameRuleMapping.resolveFromMapping(gameRule));
    }

    public static <T> T convertGameRuleHolder(GameRuleHolder holder, Class<T> newType) {
        if (gameRuleMapping == null) {
            throw new UnsupportedOperationException("GameRuleMapping is not initialized yet.");
        }
        return gameRuleMapping.gameRuleConverter.convert(holder, newType);
    }
}
