package org.screamingsandals.lib.material.data;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public interface ItemData {
    ItemData EMPTY = new EmptyItemData();

    Set<String> getKeys();

    <T> void set(String key, T data, Class<T> tClass);

    @Nullable
    <T> T get(String key, Class<T> tClass);

    <T> Optional<T> getOptional(String key, Class<T> tClass);

    <T> T getOrDefault(String key, Class<T> tClass, Supplier<T> def);

    boolean contains(String key);

    boolean isEmpty();
}
