/*
 * Copyright 2026 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.utils;

import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.api.Wrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Converts Wrapper to Platform and via versa.
 * @param <SpecificWrapper> given wrapper
 */
@NoArgsConstructor(staticName = "build")
public final class BidirectionalConverter<SpecificWrapper extends Wrapper> {
    private final @NotNull Map<@NotNull Class<?>, Function<@NotNull Object, @Nullable SpecificWrapper>> p2wConverters = new HashMap<>();
    private final @NotNull Map<@NotNull Class<?>, Function<@NotNull SpecificWrapper, @Nullable Object>> w2pConverters = new HashMap<>();


    // Caches resolved converters for fast lookup
    private final @NotNull Map<@NotNull Class<?>, Function<Object, SpecificWrapper>> p2wCache = new ConcurrentHashMap<>();
    private final @NotNull Map<@NotNull Class<?>, Map<Class<?>, Function<SpecificWrapper, Object>>> w2pCache = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <P> @NotNull BidirectionalConverter<SpecificWrapper> registerW2P(@NotNull Class<P> type, @NotNull Function<@NotNull SpecificWrapper, @Nullable P> convertor) {
        w2pConverters.put(type, (Function<SpecificWrapper, Object>) convertor);
        w2pCache.clear();
        return this;
    }

    @SuppressWarnings("unchecked")
    public <P> @NotNull BidirectionalConverter<SpecificWrapper> registerP2W(@NotNull Class<P> type, @NotNull Function<@NotNull P, @Nullable SpecificWrapper> convertor) {
        p2wConverters.put(type, (Function<Object, SpecificWrapper>) convertor);
        p2wCache.clear();
        return this;
    }

    public <P> @NotNull SpecificWrapper convert(@NotNull P object) {
        var result = convertNullable(object);
        if (result == null) {
            throw new UnsupportedOperationException(
                    "Can't convert " + object.getClass().getName() + " to the wrapper"
            );
        }
        return result;
    }

    public <P> @Nullable SpecificWrapper convertNullable(@Nullable P object) {
        if (object == null) {
            return null;
        }

        var type = object.getClass();

        // fast path via cache
        var converter = p2wCache.get(type);
        if (converter == null) {
            for (var entry : p2wConverters.entrySet()) {
                if (entry.getKey().isAssignableFrom(type)) {
                    converter = entry.getValue();
                    p2wCache.put(type, converter);
                    break;
                }
            }
        }

        if (converter == null) {
            return null;
        }

        return converter.apply(object);
    }

    public <P> @NotNull Optional<SpecificWrapper> convertOptional(@Nullable P object) {
        return Optional.ofNullable(convertNullable(object));
    }

    public <P> @NotNull P convert(@NotNull SpecificWrapper object, @NotNull Class<P> newType) {
        var result = convertNullable(object, newType);
        if (result == null) {
            throw new UnsupportedOperationException(
                    "Can't convert wrapper " + object.getClass().getName() + " to " + newType.getName()
            );
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public <P> @Nullable P convertNullable(@Nullable SpecificWrapper object, @NotNull Class<P> newType) {
        if (object == null) {
            return null;
        }

        if (newType.isInstance(object)) {
            return (P) object;
        }

        var perTypeCache = w2pCache.computeIfAbsent(object.getClass(), k -> new ConcurrentHashMap<>());

        // fast path via cache
        var converter = perTypeCache.get(newType);
        if (converter == null) {
            for (var entry : w2pConverters.entrySet()) {
                if (newType.isAssignableFrom(entry.getKey())) {
                    converter = entry.getValue();
                    perTypeCache.put(newType, converter);
                    break;
                }
            }
        }

        if (converter == null) {
            return null;
        }

        return (P) converter.apply(object);
    }

    public <P> @NotNull Optional<P> convertOptional(@Nullable SpecificWrapper object, @NotNull Class<P> newType) {
        return Optional.ofNullable(convertNullable(object, newType));
    }
}
