package org.screamingsandals.lib.utils;

import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@NoArgsConstructor(staticName = "build")
public class BidirectionalConverter<Wrapper> {
    private final Map<Class<?>, Function<Object, Wrapper>> p2wConverters = new HashMap<>();
    private final Map<Class<?>, Function<Wrapper, Object>> w2pConverters = new HashMap<>();
    private boolean finished = false;

    public void finish() {
        finished = true;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <P> BidirectionalConverter<Wrapper> registerW2P(@NotNull Class<P> type, @NotNull Function<Wrapper, P> convertor) {
        if (finished) {
            throw new UnsupportedOperationException("Converter has been already fully initialized!");
        }
        w2pConverters.put(type, (Function<Wrapper, Object>) convertor);
        return this;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <P> BidirectionalConverter<Wrapper> registerP2W(@NotNull Class<P> type, @NotNull Function<P, Wrapper> convertor) {
        if (finished) {
            throw new UnsupportedOperationException("Converter has been already fully initialized!");
        }
        p2wConverters.put(type, (Function<Object, Wrapper>) convertor);
        return this;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <P> Wrapper convert(@NotNull P object) {
        var opt = p2wConverters.entrySet()
                .stream()
                .filter(c -> c.getKey().isInstance(object))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Can't convert object to the wrapper"));
        return opt.getValue().apply(object);
    }

    @NotNull
    public <P> Optional<Wrapper> convertOptional(@Nullable P object) {
        if (object == null) {
            return Optional.empty();
        }
        var opt = p2wConverters.entrySet()
                .stream()
                .filter(c -> c.getKey().isInstance(object))
                .findFirst();
        return opt.map(classFunctionEntry -> classFunctionEntry.getValue().apply(object));
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <P> P convert(@NotNull Wrapper object, @NotNull Class<P> newType) {
        var opt = w2pConverters.entrySet()
                .stream()
                .filter(c -> newType.isAssignableFrom(c.getKey()))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Can't convert wrapper to " + newType.getName()));
        return (P) opt.getValue().apply(object);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <P> Optional<P> convertOptional(@Nullable Wrapper object, @NotNull Class<P> newType) {
        if (object == null) {
            return Optional.empty();
        }
        var opt = w2pConverters.entrySet()
                .stream()
                .filter(c -> newType.isAssignableFrom(c.getKey()))
                .findFirst();
        return opt.map(classFunctionEntry -> (P) classFunctionEntry.getValue().apply(object));
    }

}
