/*
 * Copyright 2022 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Indicates something that can be wrapped and converted to different types of choice.
 */
public interface Wrapper {
    /**
     * Converts the wrapped object to represent a subclass of the specified class object.
     *
     * @param type the class to convert the wrapped object to
     * @param <T> the type to cast the class object of the wrapped object to
     * @return this wrapped object cast to represent a subclass of the specified class object.
     * @throws RuntimeException if it is unknown how to convert the wrapper to the specific type
     */
    <T> @NotNull T as(@NotNull Class<T> type);

    /**
     * Converts the wrapped object to represent a subclass of the specified class object.
     * Returns {@link Optional#empty()} if failed to do so.
     *
     * @param type the class to convert the wrapped object to
     * @param <T> the type to cast the class object of the wrapped object to
     * @return this wrapped object cast to represent a subclass of the specified class object.
     */
    default <T> @NotNull Optional<T> asOptional(@NotNull Class<T> type) {
        try {
            return Optional.of(as(type));
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    /**
     * Converts the wrapped object to represent a subclass of the specified class object.
     * Returns {@link Optional#empty()} if failed to do so.
     *
     * @param type the class to convert the wrapped object to
     * @param <T> the type to cast the class object of the wrapped object to
     * @return this wrapped object cast to represent a subclass of the specified class object.
     */
    default <T> @Nullable T asNullable(Class<T> type) {
        try {
            return as(type);
        } catch (Exception ignored) {
            return null;
        }
    }
}
