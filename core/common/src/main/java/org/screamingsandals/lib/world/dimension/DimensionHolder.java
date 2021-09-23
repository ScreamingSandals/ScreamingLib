package org.screamingsandals.lib.world.dimension;

import lombok.Data;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
@Data
public class DimensionHolder implements ComparableWrapper {
    private final String platformName;

    @Override
    public <T> T as(Class<T> type) {
        return DimensionMapping.convertDimensionHolder(this, type);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DIMENSION)
    public boolean is(Object object) {
        return equals(ofOptional(object).orElse(null));
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DIMENSION)
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DIMENSION)
    public static DimensionHolder of(Object dimension) {
        return ofOptional(dimension).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DIMENSION)
    public static Optional<DimensionHolder> ofOptional(Object dimension) {
        if (dimension instanceof DimensionHolder) {
            return Optional.of((DimensionHolder) dimension);
        }
        return DimensionMapping.resolve(dimension);
    }

    public static List<DimensionHolder> all() {
        return DimensionMapping.getValues();
    }
}
