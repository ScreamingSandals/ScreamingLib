package org.screamingsandals.lib.utils;

import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor(staticName = "of")
public class ImmutableObjectLink<T> {
    private final Supplier<T> getter;
    private boolean cached = false;
    private T cache;

    public T get() {
        if (!cached) {
            cache = getter.get();
            cached = true;
        }
        return cache;
    }
}
