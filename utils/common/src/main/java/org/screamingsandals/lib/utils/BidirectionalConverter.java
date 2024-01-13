/*
 * Copyright 2024 ScreamingSandals
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
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Converts Wrapper to Platform and via versa.
 * @param <SpecificWrapper> given wrapper
 */
@NoArgsConstructor(staticName = "build")
public final class BidirectionalConverter<SpecificWrapper extends Wrapper> {
    private final @NotNull Map<@NotNull Class<?>, Function<@NotNull Object, @Nullable SpecificWrapper>> p2wConverters = new HashMap<>();
    private final @NotNull Map<@NotNull Class<?>, Function<@NotNull SpecificWrapper, @Nullable Object>> w2pConverters = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <P> @NotNull BidirectionalConverter<SpecificWrapper> registerW2P(@NotNull Class<P> type, @NotNull Function<@NotNull SpecificWrapper, @Nullable P> convertor) {
        w2pConverters.put(type, (Function<SpecificWrapper, Object>) convertor);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <P> @NotNull BidirectionalConverter<SpecificWrapper> registerP2W(@NotNull Class<P> type, @NotNull Function<@NotNull P, @Nullable SpecificWrapper> convertor) {
        p2wConverters.put(type, (Function<Object, SpecificWrapper>) convertor);
        return this;
    }

    public <P> @NotNull SpecificWrapper convert(@NotNull P object) {
       return convertOptional(object).orElseThrow(() -> new UnsupportedOperationException("Can't convert " + object.getClass().getName() + " to the wrapper"));
    }

    public <P> @NotNull Optional<SpecificWrapper> convertOptional(@Nullable P object) {
        if (object == null) {
            return Optional.empty();
        }

        return p2wConverters.entrySet()
                .stream()
                .filter(c -> c.getKey().isInstance(object))
                .map(entry -> entry.getValue().apply(object))
                .filter(Objects::nonNull)
                .findFirst();
    }

    public <P> @Nullable SpecificWrapper convertNullable(@Nullable P object) {
        return convertOptional(object).orElse(null);
    }

    public <P> @NotNull P convert(@NotNull SpecificWrapper object, @NotNull Class<P> newType) {
        return convertOptional(object, newType).orElseThrow(() ->
                new UnsupportedOperationException("Can't convert wrapper " + object.getClass().getName() + " to " + newType.getName()));
    }

    @SuppressWarnings("unchecked")
    public <P> @NotNull Optional<P> convertOptional(@Nullable SpecificWrapper object, @NotNull Class<P> newType) {
        if (object == null) {
            return Optional.empty();
        }
        if (newType.isInstance(object)) {
            return Optional.of((P) object);
        }
        return w2pConverters.entrySet()
                .stream()
                .filter(c -> newType.isAssignableFrom(c.getKey()))
                .map(entry -> (P) entry.getValue().apply(object))
                .filter(Objects::nonNull)
                .findFirst();
    }
}
