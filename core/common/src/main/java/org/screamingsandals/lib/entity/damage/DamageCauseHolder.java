package org.screamingsandals.lib.entity.damage;

import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
public interface DamageCauseHolder extends ComparableWrapper {

    String platformName();

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.DAMAGE_CAUSE)
    boolean is(Object damageCause);

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.DAMAGE_CAUSE)
    boolean is(Object... damageCauses);

    @CustomAutocompletion(CustomAutocompletion.Type.DAMAGE_CAUSE)
    static DamageCauseHolder of(Object damageCause) {
        return ofOptional(damageCause).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DAMAGE_CAUSE)
    static Optional<DamageCauseHolder> ofOptional(Object damageCause) {
        if (damageCause instanceof DamageCauseHolder) {
            return Optional.of((DamageCauseHolder) damageCause);
        }
        return DamageCauseMapping.resolve(damageCause);
    }

    static List<DamageCauseHolder> all() {
        return DamageCauseMapping.getValues();
    }
}
