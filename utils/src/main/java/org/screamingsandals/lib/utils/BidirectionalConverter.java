package org.screamingsandals.lib.utils;

import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Converts Wrapper to Platform and via versa.
 * @param <SpecificWrapper> given wrapper
 */
@NoArgsConstructor(staticName = "build")
public class BidirectionalConverter<SpecificWrapper extends Wrapper> {
    private final Map<Class<?>, Function<Object, SpecificWrapper>> p2wConverters = new HashMap<>();
    private final Map<Class<?>, Function<SpecificWrapper, Object>> w2pConverters = new HashMap<>();
    private boolean finished = false;

    public void finish() {
        finished = true;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <P> BidirectionalConverter<SpecificWrapper> registerW2P(@NotNull Class<P> type, @NotNull Function<SpecificWrapper, P> convertor) {
        if (finished) {
            throw new UnsupportedOperationException("Converter has been already fully initialized!");
        }
        w2pConverters.put(type, (Function<SpecificWrapper, Object>) convertor);
        return this;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <P> BidirectionalConverter<SpecificWrapper> registerP2W(@NotNull Class<P> type, @NotNull Function<P, SpecificWrapper> convertor) {
        if (finished) {
            throw new UnsupportedOperationException("Converter has been already fully initialized!");
        }
        p2wConverters.put(type, (Function<Object, SpecificWrapper>) convertor);
        return this;
    }

    @NotNull
    public <P> SpecificWrapper convert(@NotNull P object) {
       return convertOptional(object).orElseThrow(() -> new UnsupportedOperationException("Can't convert " + object.getClass().getName() + " to the wrapper"));
    }

    @NotNull
    public <P> Optional<SpecificWrapper> convertOptional(@Nullable P object) {
        if (object == null) {
            return Optional.empty();
        }
        return p2wConverters.entrySet()
                .stream()
                .filter(c -> c.getKey().isInstance(object))
                .findFirst()
                .map(entry -> entry.getValue().apply(object));
    }

    @NotNull
    public <P> P convert(@NotNull SpecificWrapper object, @NotNull Class<P> newType) {
        return convertOptional(object, newType).orElseThrow(() -> new UnsupportedOperationException("Can't convert wrapper to " + newType.getName()));
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <P> Optional<P> convertOptional(@Nullable SpecificWrapper object, @NotNull Class<P> newType) {
        if (object == null) {
            return Optional.empty();
        }
        if (newType.isInstance(object)) {
            return Optional.of((P) object);
        }
        return w2pConverters.entrySet()
                .stream()
                .filter(c -> newType.isAssignableFrom(c.getKey()))
                .findFirst()
                .map(entry -> (P) entry.getValue().apply(object));
    }

}
