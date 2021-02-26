package org.screamingsandals.lib.utils;

import java.util.Objects;

public interface QuadConsumer<T, U, V, W> {

    void accept(T t, U u, V v, W w);

    default QuadConsumer<T, U, V, W> andThen(QuadConsumer<? super T, ? super U, ? super V, ? super W> after) {
        Objects.requireNonNull(after);

        return (l, r, v, w) -> {
            accept(l, r, v, w);
            after.accept(l, r, v, w);
        };
    }
}
