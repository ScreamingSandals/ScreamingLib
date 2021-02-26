package org.screamingsandals.lib.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class Triple<F, S, T>  {
    private final F first;
    private final S second;
    private final T third;

    public static <F, S, T> Triple<F, S, T> of(F first, S second, T third) {
        return new Triple<>(first, second, third);
    }

    public static <F, S, T> Triple<F, S, T> empty() {
        return new Triple<>(null, null, null);
    }

    public boolean areAllPresent() {
        return first != null && second != null && third != null;
    }

    public boolean isPresent() {
        return first != null || second != null || third != null;
    }

    public boolean isEmpty() {
        return first == null && second == null && third == null;
    }

}
