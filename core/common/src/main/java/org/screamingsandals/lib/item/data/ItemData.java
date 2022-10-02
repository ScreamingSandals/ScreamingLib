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

package org.screamingsandals.lib.item.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Set;
import java.util.function.Supplier;

public interface ItemData {
    @NotNull Set<@NotNull NamespacedMappingKey> getKeys();

    <T> void set(@NotNull String key, @NotNull T data, @NotNull Class<T> tClass);

    <T> void set(@NotNull NamespacedMappingKey key, @NotNull T data, @NotNull Class<T> tClass);

    <T> @Nullable T get(@NotNull String key, @NotNull Class<T> tClass);

    <T> @Nullable T get(@NotNull NamespacedMappingKey key, @NotNull Class<T> tClass);

    default <T> @NotNull T getOrDefault(@NotNull String key, @NotNull Class<T> tClass, @NotNull Supplier<@NotNull T> def) {
        var value = get(key, tClass);
        return value != null ? value : def.get();
    }

    default <T> @NotNull T getOrDefault(@NotNull String key, @NotNull Class<T> tClass, @NotNull T def) {
        var value = get(key, tClass);
        return value != null ? value : def;
    }

    boolean contains(@NotNull String key);

    boolean contains(@NotNull NamespacedMappingKey key);

    boolean isEmpty();
}
