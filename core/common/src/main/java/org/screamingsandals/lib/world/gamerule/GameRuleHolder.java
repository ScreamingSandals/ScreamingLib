package org.screamingsandals.lib.world.gamerule;

import lombok.Data;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
@Data
public class GameRuleHolder implements ComparableWrapper {
    private final String platformName;

    @Override
    public <T> T as(Class<T> type) {
        return GameRuleMapping.convertGameRuleHolder(this, type);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.GAME_RULE)
    public boolean is(Object object) {
        return equals(ofOptional(object).orElse(null));
    }

    @CustomAutocompletion(CustomAutocompletion.Type.GAME_RULE)
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.GAME_RULE)
    public static GameRuleHolder of(Object gameRule) {
        return ofOptional(gameRule).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.GAME_RULE)
    public static Optional<GameRuleHolder> ofOptional(Object gameRule) {
        if (gameRule instanceof GameRuleHolder) {
            return Optional.of((GameRuleHolder) gameRule);
        }
        return GameRuleMapping.resolve(gameRule);
    }

    public static List<GameRuleHolder> all() {
        return GameRuleMapping.getValues();
    }
}
