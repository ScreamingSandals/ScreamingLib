package org.screamingsandals.lib.utils;

import java.util.Optional;

/**
 * Indicates something that can be wrapped and converted to different types of choice.
 */
public interface Wrapper {
    /**
     * Converts the WrappedObject to represent a subclass of the specified class object.
     *
     * @param type the class to convert the wrapped object to
     * @param <T> the type to cast the class object of the wrapped object to
     * @return this wrapped object cast to represent a subclass of the specified class object.
     */
    <T> T as(Class<T> type);

    /**
     * Converts the WrappedObject to represent a subclass of the specified class object.
     * Returns {@link Optional#empty()} if failed to do so.
     *
     * @param type the class to convert the wrapped object to
     * @param <T> the type to cast the class object of the wrapped object to
     * @return this wrapped object cast to represent a subclass of the specified class object.
     */
    default <T> Optional<T> asOptional(Class<T> type) {
        try {
            return Optional.ofNullable(as(type));
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }
}
