package org.screamingsandals.lib.material.data;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

class EmptyItemData implements ItemData {
    private final static List<String> EMPTY = List.of();

    @Override
    public List<String> getKeys() {
        return EMPTY;
    }

    @Override
    public <T> void set(String key, T data, Class<T> tClass) {

    }

    @Override
    public <T> T get(String key, Class<T> tClass) {
        return null;
    }

    @Override
    public <T> Optional<T> getOptional(String key, Class<T> tClass) {
        return Optional.empty();
    }

    @Override
    public <T> T getOrDefault(String key, Class<T> tClass, Supplier<T> def) {
        return def.get();
    }

    @Override
    public boolean contains(String key) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}
