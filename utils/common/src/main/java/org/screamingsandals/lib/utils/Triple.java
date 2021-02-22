package org.screamingsandals.lib.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class Triple<F, M, L>  {
    private final F first;
    private final M second;
    private final L third;

    public static <F, M, L> Triple<F, M, L> of(F first, M second, L third) {
        return new Triple<>(first, second, third);
    }
}
