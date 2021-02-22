package org.screamingsandals.lib.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class Pair<F, S> {
    private final F first;
    private final S second;

    public static <X, D> Pair<X, D> of(X first, D second) {
        return new Pair<>(first, second);
    }
}
