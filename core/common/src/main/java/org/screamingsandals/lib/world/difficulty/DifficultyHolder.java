package org.screamingsandals.lib.world.difficulty;

import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
public interface DifficultyHolder extends ComparableWrapper, RawValueHolder {

    String platformName();

    @CustomAutocompletion(CustomAutocompletion.Type.DIFFICULTY)
    boolean is(Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.DIFFICULTY)
    boolean is(Object... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.DIFFICULTY)
    static DifficultyHolder of(Object difficulty) {
        return ofOptional(difficulty).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DIFFICULTY)
    static Optional<DifficultyHolder> ofOptional(Object difficulty) {
        if (difficulty instanceof DifficultyHolder) {
            return Optional.of((DifficultyHolder) difficulty);
        }
        return DifficultyMapping.resolve(difficulty);
    }

    static List<DifficultyHolder> all() {
        return DifficultyMapping.getValues();
    }
}
