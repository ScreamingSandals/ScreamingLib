package org.screamingsandals.lib.entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DataWatcher {
    public final Map<Integer, Item<?>> entries = new HashMap<>();

    public abstract <T> void register(Item<T> item);

    @SuppressWarnings("unchecked")
    public <T> T getIndex(int index, T defaultValue) {
        Item<?> value = entries.get(index);
        return value != null ? (T) value.getValue() : defaultValue;
    }

    public <T> void setIndex(int index, T value) {
        final var item = Item.of(index, value);
        entries.put(index, item);
        register(item);
    }

    public abstract <T> void set(Item<T> item);

    public List<Item<?>> getRegisteredItems() {
        return List.copyOf(entries.values());
    }

    @RequiredArgsConstructor(staticName = "of")
    @Getter
    public static class Item<T> {
        private final int index;
        private final T value;
    }
}
