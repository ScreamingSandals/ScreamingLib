package org.screamingsandals.lib.minestom.material.builder;

import net.minestom.server.data.DataImpl;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.item.data.ItemData;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class MinestomItemData implements ItemData {
    private final static String KEY = "screaminglib::";
    private final ItemStack item;

    public MinestomItemData(ItemStack item) {
        this.item = item;
    }

    @Override
    public Set<String> getKeys() {
        final var itemData = item.getData();
        if (itemData == null) {
            return Set.of();
        }
        return Set.copyOf(itemData.getKeys());
    }

    @Override
    public <T> void set(String key, T data, Class<T> tClass) {
        if (item.getData() == null) {
            item.setData(new DataImpl());
        }

        final var itemData = item.getData();
        itemData.set(buildKey(key), data, tClass);
    }

    @Override
    public <T> @Nullable T get(String key, Class<T> tClass) {
        if (item.getData() == null) {
            return null;
        }
        return item.getData().get(buildKey(key));
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
        if (item.getData() == null) {
            return false;
        }

        return item.getData().get(buildKey(key)) != null;
    }

    @Override
    public boolean isEmpty() {
        if (item.getData() == null) {
            return true;
        }

        return item.getData().isEmpty();
    }

    private String buildKey(String key) {
        return KEY + key;
    }
}
