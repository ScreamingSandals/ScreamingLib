package org.screamingsandals.lib.bukkit.metadata;

import lombok.Data;

import java.util.function.BiFunction;
import java.util.function.Function;

@Data
public class MetadataConverter<T> {
    private final Class<T> requiredClass;
    private final Function<Object, T> mapToWrapper;
    private final BiFunction<T, Class<?>, Object> mapToPlatform;
}
