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
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagAdapterContext;
import org.bukkit.inventory.meta.tags.ItemTagType;
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

@RequiredArgsConstructor
public class BukkitItemDataCustomTags implements ItemData {
    private static final @NotNull List<Class<?>> BASE_TAGS = List.of(Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, String.class, int[].class, byte[].class, long[].class);

    @Getter
    private final @NotNull CustomItemTagContainer dataContainer;

    public static boolean isWrapperType(@NotNull Class<?> clazz) {
        return BASE_TAGS.contains(clazz);
    }

    @Override
    public @NotNull Set<@NotNull NamespacedMappingKey> getKeys() {
        throw new UnsupportedOperationException("Not implemented.");
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
                dataContainer.setCustomTag(namespacedKey, ItemTagType.STRING, s);
                return;
            }

            if (data instanceof Byte) {
                final var s = (Byte) data;
                dataContainer.setCustomTag(namespacedKey, ItemTagType.BYTE, s);
                return;
            }

            if (data instanceof Short) {
                final var s = (Short) data;
                dataContainer.setCustomTag(namespacedKey, ItemTagType.SHORT, s);
                return;
            }

            if (data instanceof Integer) {
                final var s = (Integer) data;
                dataContainer.setCustomTag(namespacedKey, ItemTagType.INTEGER, s);
                return;
            }

            if (data instanceof Long) {
                final var s = (Long) data;
                dataContainer.setCustomTag(namespacedKey, ItemTagType.LONG, s);
                return;
            }

            if (data instanceof Float) {
                final var s = (Float) data;
                dataContainer.setCustomTag(namespacedKey, ItemTagType.FLOAT, s);
                return;
            }

            if (data instanceof Double) {
                final var s = (Double) data;
                dataContainer.setCustomTag(namespacedKey, ItemTagType.DOUBLE, s);
                return;
            }

            if (data instanceof byte[]) {
                final var s = (byte[]) data;
                dataContainer.setCustomTag(namespacedKey, ItemTagType.BYTE_ARRAY, s);
                return;
            }

            if (data instanceof int[]) {
                final var s = (int[]) data;
                dataContainer.setCustomTag(namespacedKey, ItemTagType.INTEGER_ARRAY, s);
                return;
            }

            if (data instanceof long[]) {
                final var s = (long[]) data;
                dataContainer.setCustomTag(namespacedKey, ItemTagType.LONG_ARRAY, s);
                return;
            }

            throw new UnsupportedOperationException("This stuff is not supported!");
        }

        dataContainer.setCustomTag(namespacedKey, new JsonPersistentDataType<>(tClass), data);
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
                return (T) dataContainer.getCustomTag(namespacedKey, ItemTagType.STRING);
            }

            if (Byte.class.isAssignableFrom(tClass)) {
                return (T) dataContainer.getCustomTag(namespacedKey, ItemTagType.BYTE);
            }

            if (Short.class.isAssignableFrom(tClass)) {
                return (T) dataContainer.getCustomTag(namespacedKey, ItemTagType.SHORT);
            }

            if (Integer.class.isAssignableFrom(tClass)) {
                return (T) dataContainer.getCustomTag(namespacedKey, ItemTagType.INTEGER);
            }

            if (Long.class.isAssignableFrom(tClass)) {
                return (T) dataContainer.getCustomTag(namespacedKey, ItemTagType.LONG);
            }

            if (Float.class.isAssignableFrom(tClass)) {
                return (T) dataContainer.getCustomTag(namespacedKey, ItemTagType.FLOAT);
            }

            if (Double.class.isAssignableFrom(tClass)) {
                return (T) dataContainer.getCustomTag(namespacedKey, ItemTagType.DOUBLE);
            }

            if (byte[].class.isAssignableFrom(tClass)) {
                return (T) dataContainer.getCustomTag(namespacedKey, ItemTagType.BYTE_ARRAY);
            }

            if (int[].class.isAssignableFrom(tClass)) {
                return (T) dataContainer.getCustomTag(namespacedKey, ItemTagType.INTEGER_ARRAY);
            }

            if (long[].class.isAssignableFrom(tClass)) {
                return (T) dataContainer.getCustomTag(namespacedKey, ItemTagType.LONG_ARRAY);
            }

            throw new UnsupportedOperationException("This stuff is not supported!");
        }

        return dataContainer.getCustomTag(namespacedKey, new JsonPersistentDataType<>(tClass));
    }

    @Override
    public boolean contains(@NotNull String key) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public boolean contains(@NotNull NamespacedMappingKey key) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public boolean isEmpty() {
        return dataContainer.isEmpty();
    }

    public static class JsonPersistentDataType<T> implements ItemTagType<String, T> {
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
        public @NotNull String toPrimitive(@NotNull T complex, @NotNull ItemTagAdapterContext context) {
            return GsonUtils.gson().toJson(complex);
        }

        @Override
        public @NotNull T fromPrimitive(@NotNull String primitive, @NotNull ItemTagAdapterContext context) {
            return GsonUtils.gson().fromJson(primitive, tClass);
        }
    }
}
