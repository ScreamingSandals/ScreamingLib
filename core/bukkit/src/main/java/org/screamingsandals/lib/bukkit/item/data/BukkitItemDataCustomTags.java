package org.screamingsandals.lib.bukkit.item.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagAdapterContext;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.item.data.ItemData;
import org.screamingsandals.lib.utils.GsonUtils;
import org.screamingsandals.lib.utils.Primitives;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class BukkitItemDataCustomTags implements ItemData {
    private static final List<Class<?>> BASE_TAGS = List.of(Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, String.class, int[].class, byte[].class, long[].class);

    private final Plugin plugin;
    @Getter
    private final CustomItemTagContainer dataContainer;

    public static boolean isWrapperType(Class<?> clazz) {
        return BASE_TAGS.contains(clazz);
    }

    @Override
    public Set<String> getKeys() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public <T> void set(String key, T data, Class<T> tClass) {
        if (!Primitives.isWrapperType(tClass)) {
            tClass = Primitives.wrap(tClass); //Make sure we will always "switch" over the wrapper types
        }

        final var container = this.dataContainer;
        final var namespacedKey = new NamespacedKey(plugin, key);
        if (isWrapperType(tClass)) {
            if (data instanceof String) {
                final var s = (String) data;
                container.setCustomTag(namespacedKey, ItemTagType.STRING, s);
                return;
            }

            if (data instanceof Byte) {
                final var s = (Byte) data;
                container.setCustomTag(namespacedKey, ItemTagType.BYTE, s);
                return;
            }

            if (data instanceof Short) {
                final var s = (Short) data;
                container.setCustomTag(namespacedKey, ItemTagType.SHORT, s);
                return;
            }

            if (data instanceof Integer) {
                final var s = (Integer) data;
                container.setCustomTag(namespacedKey, ItemTagType.INTEGER, s);
                return;
            }

            if (data instanceof Long) {
                final var s = (Long) data;
                container.setCustomTag(namespacedKey, ItemTagType.LONG, s);
                return;
            }

            if (data instanceof Float) {
                final var s = (Float) data;
                container.setCustomTag(namespacedKey, ItemTagType.FLOAT, s);
                return;
            }

            if (data instanceof Double) {
                final var s = (Double) data;
                container.setCustomTag(namespacedKey, ItemTagType.DOUBLE, s);
                return;
            }

            if (data instanceof byte[]) {
                final var s = (byte[]) data;
                container.setCustomTag(namespacedKey, ItemTagType.BYTE_ARRAY, s);
                return;
            }

            if (data instanceof int[]) {
                final var s = (int[]) data;
                container.setCustomTag(namespacedKey, ItemTagType.INTEGER_ARRAY, s);
                return;
            }

            if (data instanceof long[]) {
                final var s = (long[]) data;
                container.setCustomTag(namespacedKey, ItemTagType.LONG_ARRAY, s);
                return;
            }

            throw new UnsupportedOperationException("This stuff is not supported!");
        }

        container.setCustomTag(namespacedKey, new JsonPersistentDataType<>(tClass), data);
    }

    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> tClass) {
        if (!Primitives.isWrapperType(tClass)) {
            tClass = Primitives.wrap(tClass); //Make sure we will always "switch" over the wrapper types
        }

        final var container = dataContainer;
        final var namespacedKey = new NamespacedKey(plugin, key);
        if (isWrapperType(tClass)) {
            if (String.class.isAssignableFrom(tClass)) {
                return (T) container.getCustomTag(namespacedKey, ItemTagType.STRING);
            }

            if (Byte.class.isAssignableFrom(tClass)) {
                return (T) container.getCustomTag(namespacedKey, ItemTagType.BYTE);
            }

            if (Short.class.isAssignableFrom(tClass)) {
                return (T) container.getCustomTag(namespacedKey, ItemTagType.SHORT);
            }

            if (Integer.class.isAssignableFrom(tClass)) {
                return (T) container.getCustomTag(namespacedKey, ItemTagType.INTEGER);
            }

            if (Long.class.isAssignableFrom(tClass)) {
                return (T) container.getCustomTag(namespacedKey, ItemTagType.LONG);
            }

            if (Float.class.isAssignableFrom(tClass)) {
                return (T) container.getCustomTag(namespacedKey, ItemTagType.FLOAT);
            }

            if (Double.class.isAssignableFrom(tClass)) {
                return (T) container.getCustomTag(namespacedKey, ItemTagType.DOUBLE);
            }

            if (byte[].class.isAssignableFrom(tClass)) {
                return (T) container.getCustomTag(namespacedKey, ItemTagType.BYTE_ARRAY);
            }

            if (int[].class.isAssignableFrom(tClass)) {
                return (T) container.getCustomTag(namespacedKey, ItemTagType.INTEGER_ARRAY);
            }

            if (long[].class.isAssignableFrom(tClass)) {
                return (T) container.getCustomTag(namespacedKey, ItemTagType.LONG_ARRAY);
            }

            throw new UnsupportedOperationException("This stuff is not supported!");
        }

        return container.getCustomTag(namespacedKey, new JsonPersistentDataType<>(tClass));
    }

    @Override
    public <T> Optional<T> getOptional(String key, Class<T> tClass) {
        return Optional.ofNullable(get(key, tClass));
    }

    @Override
    public <T> T getOrDefault(String key, Class<T> tClass, Supplier<T> def) {
        return getOptional(key, tClass).orElse(def.get());
    }

    @Override
    public boolean contains(String key) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public boolean isEmpty() {
        return dataContainer.isEmpty();
    }

    public static class JsonPersistentDataType<T> implements ItemTagType<String, T> {
        private final Class<T> tClass;

        public JsonPersistentDataType(Class<T> tClass) {
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
