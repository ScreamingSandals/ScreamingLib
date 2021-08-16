package org.screamingsandals.lib.world.difficulty;

import lombok.Data;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.Arrays;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
@Data
public class DifficultyHolder implements ComparableWrapper {
    private final String platformName;

    @Override
    public <T> T as(Class<T> type) {
        return DifficultyMapping.convertDifficultyHolder(this, type);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DIFFICULTY)
    public boolean is(Object object) {
        return equals(ofOptional(object).orElse(null));
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DIFFICULTY)
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DIFFICULTY)
    public static DifficultyHolder of(Object difficulty) {
        return ofOptional(difficulty).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DIFFICULTY)
    public static Optional<DifficultyHolder> ofOptional(Object difficulty) {
        if (difficulty instanceof DifficultyHolder) {
            return Optional.of((DifficultyHolder) difficulty);
        }
        return DifficultyMapping.resolve(difficulty);
    }
}
