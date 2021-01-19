package org.screamingsandals.lib.utils;

import java.util.Optional;

/**
 * Indicates something that can be wrapped
 */
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
