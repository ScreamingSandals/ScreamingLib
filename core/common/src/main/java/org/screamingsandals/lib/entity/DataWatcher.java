package org.screamingsandals.lib.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DataWatcher {
    public <T> void setIndex(int index, T value) {
        final var item = Item.of(index, value);
        register(item);
    }

    public abstract <T> void register(Item<T> item);

    public abstract <T> void set(Item<T> item);

    public abstract Object get(int index, Object serializer);

    @RequiredArgsConstructor(staticName = "of")
    @Getter
    public static class Item<T> {
        private final int index;
        private final T value;
    }
}
