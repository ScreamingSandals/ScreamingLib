/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.api.types.server;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.api.Wrapper;

import java.util.function.Function;

/**
 * A class holding a location, which can be unwrapped to a platform-specific type.
 * <p>
 * To unwrap this type to a platform-specific type, use a construction similar to the following example:
 * <pre>{@code
 *  import org.bukkit.Location; // or other type based on the platform
 *
 *  ...
 *
 *  LocationHolder locationHolder = apiManager.methodReturningLocationHolder();
 *  Location location = locationHolder.as(Location.class);
 *
 *  // or shortened
 *  Location location = apiManager.methodReturningLocationHolder().as(Location.class);
 * }</pre>
 * <p>
 * To create a new {@link LocationHolder}, use a construction similar to the following example:
 * <pre>{@code
 *  import org.bukkit.Location; // or other type based on the platform
 *
 *  ...
 *
 *  Location location = ...
 *  LocationHolder locationHolder = LocationHolder.of(location);
 * }</pre>
 * <p>
 * Currently supported platform types:
 * <ul>
 *     <li>{@code org.bukkit.Location}</li>
 * </ul>
 * <p>
 * NOTE: The type should be directly used only when interacting with an API of a ScreamingLib-based plugin.
 * The ScreamingLib-based plugin itself should use the actual Component type provided by the library.
 * <p>
 * The holder itself lacks identity and should not be compared using {@code ==}, use {@link Object#equals(Object)} instead.
 *
 * @since 2.0.3
 */
@ApiStatus.NonExtendable
public interface LocationHolder extends Wrapper {
    /**
     * Converts a platform-specific type to a new {@link LocationHolder}. This method does not accept null.
     *
     * @param location a location represented by a platform-specific type
     * @return new {@link LocationHolder}
     * @throws IllegalArgumentException if the platform location object is not convertable or null has been passed
     * @throws UnsupportedOperationException if the provider has not been registered yet
     * @since 2.0.3
     */
    static @NotNull LocationHolder of(@NotNull Object location) {
        var result = ofNullable(location);
        if (result == null) {
            throw new IllegalArgumentException("Could not wrap location: " + location);
        }
        return result;
    }

    /**
     * Converts a platform-specific type to a new {@link LocationHolder}.
     *
     * @param location a location represented by a platform-specific type or null
     * @return new {@link LocationHolder} or null if the location has not been passed
     * @throws IllegalArgumentException if the platform location object is not convertable
     * @throws UnsupportedOperationException if the provider has not been registered yet
     * @since 2.0.3
     */
    static @Nullable LocationHolder ofNullable(@Nullable Object location) {
        if (Provider.provider == null) {
            throw new UnsupportedOperationException("A provider for " + LocationHolder.class.getSimpleName() + " has not been registered yet!");
        }
        if (location == null) {
            return null;
        }
        return Provider.provider.apply(location);
    }

    @ApiStatus.Internal
    final class Provider {
        private static @Nullable Function<@NotNull Object, @Nullable LocationHolder> provider;

        private Provider() {
        }

        public static void registerProvider(@NotNull Function<@NotNull Object, @Nullable LocationHolder> provider) {
            if (Provider.provider != null) {
                throw new UnsupportedOperationException("A provider for " + LocationHolder.class.getSimpleName() + " has already been registered!");
            }
            Provider.provider = provider;
        }
    }
}
