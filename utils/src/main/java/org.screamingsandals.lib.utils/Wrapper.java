package org.screamingsandals.lib.utils;

import java.util.Optional;

public interface Wrapper {
    <T> T as(Class<T> type);

    default <T> Optional<T> asOptional(Class<T> type) {
        try {
            return Optional.ofNullable(as(type));
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }
}
