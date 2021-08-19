package org.screamingsandals.lib.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Supplier;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ObjectLink<T> {
    private final Supplier<T> getter;
    private final Consumer<T> setter;

    public T get() {
        return getter.get();
    }

    public void set(T t) {
        setter.accept(t);
    }

    public static <T> ObjectLink<T> of(Supplier<T> getter, Consumer<T> setter) {
        return new ObjectLink<>(getter, setter);
    }
}
