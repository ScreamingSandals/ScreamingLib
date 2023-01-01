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

package org.screamingsandals.lib.bukkit.item.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.BukkitCore;
import org.screamingsandals.lib.item.data.ItemData;
import org.screamingsandals.lib.utils.GsonUtils;
import org.screamingsandals.lib.utils.Primitives;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BukkitItemDataPersistentContainer implements ItemData {
    private static final @NotNull List<Class<?>> BASE_TAGS = List.of(Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, String.class, int[].class, byte[].class, long[].class);

    @Getter
    private final @NotNull PersistentDataContainer dataContainer;

    public static boolean isWrapperType(@NotNull Class<?> clazz) {
        return BASE_TAGS.contains(clazz);
    }

    @Override
    public @NotNull Set<@NotNull NamespacedMappingKey> getKeys() {
        return dataContainer.getKeys()
                .stream()
                .map(k -> NamespacedMappingKey.of(k.getNamespace(), k.getKey()))
                .collect(Collectors.toSet());
    }

    @Override
    public <T> void set(@NotNull String key, @NotNull T data, @NotNull Class<T> tClass) {
        set(NamespacedMappingKey.of(BukkitCore.getPlugin().getName().toLowerCase(Locale.ROOT), key.toLowerCase(Locale.ROOT)), data, tClass);
    }

    @Override
    public <T> void set(@NotNull NamespacedMappingKey key, @NotNull T data, @NotNull Class<T> tClass) {
        if (!Primitives.isWrapperType(tClass)) {
            tClass = Primitives.wrap(tClass); //Make sure we will always "switch" over the wrapper types
        }

        final var namespacedKey = new NamespacedKey(key.namespace(), key.value());
        if (isWrapperType(tClass)) {
            if (data instanceof String) {
                final var s = (String) data;
                dataContainer.set(namespacedKey, PersistentDataType.STRING, s);
                return;
            }

            if (data instanceof Byte) {
                final var s = (Byte) data;
                dataContainer.set(namespacedKey, PersistentDataType.BYTE, s);
                return;
            }

            if (data instanceof Short) {
                final var s = (Short) data;
                dataContainer.set(namespacedKey, PersistentDataType.SHORT, s);
                return;
            }

            if (data instanceof Integer) {
                final var s = (Integer) data;
                dataContainer.set(namespacedKey, PersistentDataType.INTEGER, s);
                return;
            }

            if (data instanceof Long) {
                final var s = (Long) data;
                dataContainer.set(namespacedKey, PersistentDataType.LONG, s);
                return;
            }

            if (data instanceof Float) {
                final var s = (Float) data;
                dataContainer.set(namespacedKey, PersistentDataType.FLOAT, s);
                return;
            }

            if (data instanceof Double) {
                final var s = (Double) data;
                dataContainer.set(namespacedKey, PersistentDataType.DOUBLE, s);
                return;
            }

            if (data instanceof byte[]) {
                final var s = (byte[]) data;
                dataContainer.set(namespacedKey, PersistentDataType.BYTE_ARRAY, s);
                return;
            }

            if (data instanceof int[]) {
                final var s = (int[]) data;
                dataContainer.set(namespacedKey, PersistentDataType.INTEGER_ARRAY, s);
                return;
            }

            if (data instanceof long[]) {
                final var s = (long[]) data;
                dataContainer.set(namespacedKey, PersistentDataType.LONG_ARRAY, s);
                return;
            }

            throw new UnsupportedOperationException("This stuff is not supported!");
        }

        dataContainer.set(namespacedKey, new JsonPersistentDataType<>(tClass), data);
    }

    @Override
    public <T> @Nullable T get(@NotNull String key, @NotNull Class<T> tClass) {
        return get(NamespacedMappingKey.of(BukkitCore.getPlugin().getName().toLowerCase(Locale.ROOT), key.toLowerCase(Locale.ROOT)), tClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> @Nullable T get(@NotNull NamespacedMappingKey key, @NotNull Class<T> tClass) {
        if (!Primitives.isWrapperType(tClass)) {
            tClass = Primitives.wrap(tClass); //Make sure we will always "switch" over the wrapper types
        }

        final var namespacedKey = new NamespacedKey(key.namespace(), key.value());
        if (isWrapperType(tClass)) {
            if (String.class.isAssignableFrom(tClass)) {
                return (T) dataContainer.get(namespacedKey, PersistentDataType.STRING);
            }

            if (Byte.class.isAssignableFrom(tClass)) {
                return (T) dataContainer.get(namespacedKey, PersistentDataType.BYTE);
            }

            if (Short.class.isAssignableFrom(tClass)) {
                return (T) dataContainer.get(namespacedKey, PersistentDataType.SHORT);
            }

            if (Integer.class.isAssignableFrom(tClass)) {
                return (T) dataContainer.get(namespacedKey, PersistentDataType.INTEGER);
            }

            if (Long.class.isAssignableFrom(tClass)) {
                return (T) dataContainer.get(namespacedKey, PersistentDataType.LONG);
            }

            if (Float.class.isAssignableFrom(tClass)) {
                return (T) dataContainer.get(namespacedKey, PersistentDataType.FLOAT);
            }

            if (Double.class.isAssignableFrom(tClass)) {
                return (T) dataContainer.get(namespacedKey, PersistentDataType.DOUBLE);
            }

            if (byte[].class.isAssignableFrom(tClass)) {
                return (T) dataContainer.get(namespacedKey, PersistentDataType.BYTE_ARRAY);
            }

            if (int[].class.isAssignableFrom(tClass)) {
                return (T) dataContainer.get(namespacedKey, PersistentDataType.INTEGER_ARRAY);
            }

            if (long[].class.isAssignableFrom(tClass)) {
                return (T) dataContainer.get(namespacedKey, PersistentDataType.LONG_ARRAY);
            }

            throw new UnsupportedOperationException("This stuff is not supported!");
        }

        return dataContainer.get(namespacedKey, new JsonPersistentDataType<>(tClass));
    }

    @Override
    public boolean contains(@NotNull String key) {
        return dataContainer.getKeys()
                .stream()
                .anyMatch(next -> next.getNamespace().equalsIgnoreCase(BukkitCore.getPlugin().getName()) && next.getKey().equalsIgnoreCase(key));
    }

    @Override
    public boolean contains(@NotNull NamespacedMappingKey key) {
        return dataContainer.getKeys()
                .stream()
                .anyMatch(next -> next.getNamespace().equalsIgnoreCase(key.getNamespace()) && next.getKey().equalsIgnoreCase(key.getKey()));
    }

    @Override
    public boolean isEmpty() {
        return dataContainer.isEmpty();
    }

    public static class JsonPersistentDataType<T> implements PersistentDataType<String, T> {
        private final @NotNull Class<T> tClass;

        public JsonPersistentDataType(@NotNull Class<T> tClass) {
            this.tClass = tClass;
        }

        @Override
        public @NotNull Class<String> getPrimitiveType() {
            return String.class;
        }

        @Override
        public @NotNull Class<T> getComplexType() {
            return tClass;
        }

        @Override
        public @NotNull String toPrimitive(@NotNull T complex, @NotNull PersistentDataAdapterContext context) {
            return GsonUtils.gson().toJson(complex);
        }

        @Override
        public @NotNull T fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
            return GsonUtils.gson().fromJson(primitive, tClass);
        }
    }
}
