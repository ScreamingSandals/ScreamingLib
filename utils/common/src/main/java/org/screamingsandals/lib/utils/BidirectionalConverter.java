/*
 * Copyright 2022 ScreamingSandals
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
public class BidirectionalConverter<SpecificWrapper extends Wrapper> {
    private final Map<Class<?>, Function<Object, SpecificWrapper>> p2wConverters = new HashMap<>();
    private final Map<Class<?>, Function<SpecificWrapper, Object>> w2pConverters = new HashMap<>();
    private Class<?> normalizeType;

    @SuppressWarnings("unchecked")
    @NotNull
    public <P> BidirectionalConverter<SpecificWrapper> registerW2P(@NotNull Class<P> type, @NotNull Function<SpecificWrapper, P> convertor) {
        w2pConverters.put(type, (Function<SpecificWrapper, Object>) convertor);
        return this;
    }

    @NotNull
    public <P> BidirectionalConverter<SpecificWrapper> normalizeType(Class<P> type) {
        normalizeType = type;
        return this;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <P> BidirectionalConverter<SpecificWrapper> registerP2W(@NotNull Class<P> type, @NotNull Function<P, SpecificWrapper> convertor) {
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
                .map(entry -> entry.getValue().apply(object))
                .filter(Objects::nonNull)
                .findFirst();
    }

    public <P> @Nullable SpecificWrapper convertNullable(@Nullable P object) {
        return convertOptional(object).orElse(null);
    }

    @NotNull
    public <P> P convert(@NotNull SpecificWrapper object, @NotNull Class<P> newType) {
        return convertOptional(object, newType).orElseThrow(() ->
                new UnsupportedOperationException("Can't convert wrapper " + object.getClass().getName() + " to " + newType.getName()));
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
                .map(entry -> (P) entry.getValue().apply(object))
                .filter(Objects::nonNull)
                .findFirst();
    }

    /**
     * Normalizes the wrapper by converting to platform object and then back to wrapper.
     * This removes all fields that are not supported by platform.
     *
     * @param object which should be normalized
     * @return normalized object
     */
    @NotNull
    public SpecificWrapper normalize(@NotNull SpecificWrapper object) {
        if (normalizeType == null) {
            throw new UnsupportedOperationException("Can't normalize " + object.getClass().getName() + ", no normalization class is registered");
        }
        return convert(convert(object, normalizeType));
    }

}
