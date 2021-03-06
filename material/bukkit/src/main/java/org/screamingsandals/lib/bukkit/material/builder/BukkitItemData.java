package org.screamingsandals.lib.bukkit.material.builder;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.material.data.ItemData;
import org.screamingsandals.lib.utils.GsonUtils;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BukkitItemData implements ItemData {
    private static final List<Class<?>> WRAPPER_TYPES = List.of(Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class);

    private final Plugin plugin;
    private final ItemStack item;

    public BukkitItemData(Plugin plugin, ItemStack item) {
        this.plugin = plugin;
        this.item = item;
    }

    public static boolean isWrapperType(Class<?> clazz) {
        return WRAPPER_TYPES.contains(clazz);
    }

    @Override
    public List<String> getKeys() {
        final var container = item.getItemMeta().getPersistentDataContainer();
        return container.getKeys()
                .stream()
                .map(NamespacedKey::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public <T> void set(String key, T data, Class<T> tClass) {
        final var container = item.getItemMeta().getPersistentDataContainer();
        final var namespacedKey = new NamespacedKey(plugin, key);
        if (isWrapperType(tClass)) {
            if (data instanceof String) {
                final var s = (String) data;
                container.set(namespacedKey, PersistentDataType.STRING, s);
                return;
            }

            if (data instanceof Byte) {
                final var s = (Byte) data;
                container.set(namespacedKey, PersistentDataType.BYTE, s);
                return;
            }

            if (data instanceof Short) {
                final var s = (Short) data;
                container.set(namespacedKey, PersistentDataType.SHORT, s);
                return;
            }

            if (data instanceof Integer) {
                final var s = (Integer) data;
                container.set(namespacedKey, PersistentDataType.INTEGER, s);
                return;
            }

            if (data instanceof Long) {
                final var s = (Long) data;
                container.set(namespacedKey, PersistentDataType.LONG, s);
                return;
            }

            if (data instanceof Float) {
                final var s = (Float) data;
                container.set(namespacedKey, PersistentDataType.FLOAT, s);
                return;
            }

            if (data instanceof Double) {
                final var s = (Double) data;
                container.set(namespacedKey, PersistentDataType.DOUBLE, s);
                return;
            }

            throw new UnsupportedOperationException("This stuff is not supported!");
        }

        container.set(namespacedKey, new JsonPersistentDataType<>(tClass), data);
    }

    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> tClass) {
        final var container = item.getItemMeta().getPersistentDataContainer();
        final var namespacedKey = new NamespacedKey(plugin, key);
        if (isWrapperType(tClass)) {
            if (String.class.isAssignableFrom(tClass)) {
                return (T) container.get(namespacedKey, PersistentDataType.STRING);
            }

            if (Byte.class.isAssignableFrom(tClass)) {
                return (T) container.get(namespacedKey, PersistentDataType.BYTE);
            }

            if (Short.class.isAssignableFrom(tClass)) {
                return (T) container.get(namespacedKey, PersistentDataType.SHORT);
            }

            if (Integer.class.isAssignableFrom(tClass)) {
                return (T) container.get(namespacedKey, PersistentDataType.INTEGER);
            }

            if (Long.class.isAssignableFrom(tClass)) {
                return (T) container.get(namespacedKey, PersistentDataType.LONG);
            }

            if (Float.class.isAssignableFrom(tClass)) {
                return (T) container.get(namespacedKey, PersistentDataType.FLOAT);
            }

            if (Double.class.isAssignableFrom(tClass)) {
                return (T) container.get(namespacedKey, PersistentDataType.DOUBLE);
            }

            throw new UnsupportedOperationException("This stuff is not supported!");
        }

        return container.get(namespacedKey, new JsonPersistentDataType<>(tClass));
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
        final var container = item.getItemMeta().getPersistentDataContainer();
        final var namespacedKey = container.getKeys()
                .stream()
                .filter(next -> next.getNamespace().equalsIgnoreCase(plugin.getName().toLowerCase(Locale.ROOT)))
                .filter(next -> next.getKey().equalsIgnoreCase(key))
                .findAny();
        return namespacedKey.isPresent();
    }

    @Override
    public boolean isEmpty() {
        return item.getItemMeta().getPersistentDataContainer().isEmpty();
    }

    public static class JsonPersistentDataType<T> implements PersistentDataType<String, T> {
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
        public @NotNull String toPrimitive(@NotNull T complex, @NotNull PersistentDataAdapterContext context) {
            return GsonUtils.gson().toJson(complex);
        }

        @Override
        public @NotNull T fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
            return GsonUtils.gson().fromJson(primitive, tClass);
        }
    }
}
