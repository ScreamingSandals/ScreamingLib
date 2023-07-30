/*
 * Copyright 2023 ScreamingSandals
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

package org.screamingsandals.lib.impl.utils;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@UtilityClass
public class Primitives {
    private final @NotNull Map<@NotNull Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER_TYPE;
    private final @NotNull Map<@NotNull Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE_TYPE;

    static {
        @NotNull Map<@NotNull Class<?>, Class<?>> primToWrap = new HashMap<>(16);
        @NotNull Map<@NotNull Class<?>, Class<?>> wrapToPrim = new HashMap<>(16);

        add(primToWrap, wrapToPrim, boolean.class, Boolean.class);
        add(primToWrap, wrapToPrim, byte.class, Byte.class);
        add(primToWrap, wrapToPrim, char.class, Character.class);
        add(primToWrap, wrapToPrim, double.class, Double.class);
        add(primToWrap, wrapToPrim, float.class, Float.class);
        add(primToWrap, wrapToPrim, int.class, Integer.class);
        add(primToWrap, wrapToPrim, long.class, Long.class);
        add(primToWrap, wrapToPrim, short.class, Short.class);
        add(primToWrap, wrapToPrim, void.class, Void.class);

        PRIMITIVE_TO_WRAPPER_TYPE = Collections.unmodifiableMap(primToWrap);
        WRAPPER_TO_PRIMITIVE_TYPE = Collections.unmodifiableMap(wrapToPrim);
    }

    private static void add(
            @NotNull Map<@NotNull Class<?>, Class<?>> forward,
            @NotNull Map<@NotNull Class<?>, Class<?>> backward,
            @NotNull Class<?> key,
            @NotNull Class<?> value
    ) {
        forward.put(key, value);
        backward.put(value, key);
    }

    public @NotNull Set<@NotNull Class<?>> allPrimitiveTypes() {
        return PRIMITIVE_TO_WRAPPER_TYPE.keySet();
    }

    public @NotNull Set<@NotNull Class<?>> allWrapperTypes() {
        return WRAPPER_TO_PRIMITIVE_TYPE.keySet();
    }

    public boolean isWrapperType(@NotNull Class<?> type) {
        return WRAPPER_TO_PRIMITIVE_TYPE.containsKey(type);
    }

    @SuppressWarnings("unchecked")
    public <T> @NotNull Class<T> wrap(@NotNull Class<T> type) {
        final Class<T> wrapped = (Class<T>) PRIMITIVE_TO_WRAPPER_TYPE.get(type);
        return (wrapped == null) ? type : wrapped;
    }

    @SuppressWarnings("unchecked")
    public <T> @NotNull Class<T> unwrap(@NotNull Class<T> type) {
        final Class<T> unwrapped = (Class<T>) WRAPPER_TO_PRIMITIVE_TYPE.get(type);
        return (unwrapped == null) ? type : unwrapped;
    }
}
