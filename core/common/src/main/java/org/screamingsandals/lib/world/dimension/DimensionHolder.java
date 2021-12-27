package org.screamingsandals.lib.world.dimension;

import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
public interface DimensionHolder extends ComparableWrapper, RawValueHolder {

    String platformName();

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.DIMENSION)
    boolean is(Object object);

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.DIMENSION)
    boolean is(Object... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.DIMENSION)
     static DimensionHolder of(Object dimension) {
        return ofOptional(dimension).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DIMENSION)
     static Optional<DimensionHolder> ofOptional(Object dimension) {
        if (dimension instanceof DimensionHolder) {
            return Optional.of((DimensionHolder) dimension);
        }
        return DimensionMapping.resolve(dimension);
    }

     static List<DimensionHolder> all() {
        return DimensionMapping.getValues();
    }
}
