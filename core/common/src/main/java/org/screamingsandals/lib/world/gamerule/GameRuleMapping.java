package org.screamingsandals.lib.world.gamerule;

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
public abstract class GameRuleMapping extends AbstractTypeMapper<GameRuleHolder> {
    private static GameRuleMapping gameRuleMapping;

    protected final BidirectionalConverter<GameRuleHolder> gameRuleConverter = BidirectionalConverter.<GameRuleHolder>build()
            .registerP2W(GameRuleHolder.class, d -> d);

    @ApiStatus.Internal
    public GameRuleMapping() {
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

    @OfMethodAlternative(value = GameRuleHolder.class, methodName = "all")
    public static List<GameRuleHolder> getValues() {
        if (gameRuleMapping == null) {
            throw new UnsupportedOperationException("GameRuleMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(gameRuleMapping.values);
    }
}
