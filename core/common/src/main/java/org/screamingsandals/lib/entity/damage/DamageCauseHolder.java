package org.screamingsandals.lib.entity.damage;

import lombok.Data;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.Arrays;
import java.util.Optional;

@Data
public class DamageCauseHolder implements Wrapper {
    private final String platformName;

    @Override
    public <T> T as(Class<T> type) {
        return DamageCauseMapping.convertDamageCauseHolder(this, type);
    }

    public boolean is(Object damageCause) {
        return equals(DamageCauseMapping.resolve(damageCause).orElse(null));
    }

    public boolean is(Object... damageCauses) {
        return Arrays.stream(damageCauses).anyMatch(this::is);
    }

    public static DamageCauseHolder of(Object damageCause) {
        return ofOptional(damageCause).orElseThrow();
    }

    public static Optional<DamageCauseHolder> ofOptional(Object damageCause) {
        if (damageCause instanceof DamageCauseHolder) {
            return Optional.of((DamageCauseHolder) damageCause);
        }
        return DamageCauseMapping.resolve(damageCause);
    }
}
