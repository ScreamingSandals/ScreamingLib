package org.screamingsandals.lib.utils;

import java.util.Objects;

public interface TriConsumer<T, U, V> {

    void accept(T t, U u, V v);

    default TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after) {
        Objects.requireNonNull(after);

        return (l, r, v) -> {
            accept(l, r, v);
            after.accept(l, r, v);
        };
    }
}
