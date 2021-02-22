package org.screamingsandals.lib.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class Pair<F, S> {
    private final F first;
    private final S second;

    public static <F, S> Pair<F, S> of(F first, S second) {
        return new Pair<>(first, second);
    }

    public static <F, S> Pair<F, S> empty() {
        return new Pair<>(null, null);
    }

    public boolean areBothPresent() {
        return first != null && second != null;
    }

    public boolean isPresent() {
        return first != null || second != null;
    }

    public boolean isEmpty() {
        return first == null && second == null;
    }
}
