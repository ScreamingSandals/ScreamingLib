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
 * A class holding a container, which can be unwrapped to a platform-specific type.
 * <p>
 * To unwrap this type to a platform-specific type, use a construction similar to the following example:
 * <pre>{@code
 *  import org.bukkit.inventory.Inventory; // or other type based on the platform
 *
 *  ...
 *
 *  ContainerHolder containerHolder = apiManager.methodReturningContainerHolder();
 *  Inventory inventory = containerHolder.as(Inventory.class);
 *
 *  // or shortened
 *  Inventory inventory = apiManager.methodReturningContainerHolder().as(Inventory.class);
 * }</pre>
 * <p>
 * To create a new {@link ContainerHolder}, use a construction similar to the following example:
 * <pre>{@code
 *  import org.bukkit.inventory.Inventory; // or other type based on the platform
 *
 *  ...
 *
 *  Inventory inventory = ...
 *  ContainerHolder containerHolder = ContainerHolder.of(location);
 * }</pre>
 * <p>
 * Currently supported platform types:
 * <ul>
 *     <li>{@code org.bukkit.inventory.Inventory}</li>
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
@ApiStatus.Experimental // the semantical meaning of this type may slightly change in the future
public interface ContainerHolder extends Wrapper {
    /**
     * Converts a platform-specific object to a new {@link ContainerHolder}. This method does not accept null.
     *
     * @param container a container represented by a platform-specific type
     * @return new {@link ContainerHolder}
     * @throws IllegalArgumentException if the platform container object is not convertable or null has been passed
     * @throws UnsupportedOperationException if the provider has not been registered yet
     * @since 2.0.3
     */
    static @NotNull ContainerHolder of(@NotNull Object container) {
        var result = ofNullable(container);
        if (result == null) {
            throw new IllegalArgumentException("Could not wrap container: " + container);
        }
        return result;
    }

    /**
     * Converts a platform-specific object to a new {@link ContainerHolder}.
     *
     * @param container a container represented by a platform-specific type or null
     * @return new {@link ContainerHolder} or null if the container has not been passed
     * @throws IllegalArgumentException if the platform container object is not convertable
     * @throws UnsupportedOperationException if the provider has not been registered yet
     * @since 2.0.3
     */
    static @Nullable ContainerHolder ofNullable(@Nullable Object container) {
        if (Provider.provider == null) {
            throw new UnsupportedOperationException("A provider for " + ContainerHolder.class.getSimpleName() + " has not been registered yet!");
        }
        if (container == null) {
            return null;
        }
        return Provider.provider.apply(container);
    }

    @ApiStatus.Internal
    final class Provider {
        private static @Nullable Function<@NotNull Object, @Nullable ContainerHolder> provider;

        private Provider() {
        }

        public static void registerProvider(@NotNull Function<@NotNull Object, @Nullable ContainerHolder> provider) {
            if (Provider.provider != null) {
                throw new UnsupportedOperationException("A provider for " + ContainerHolder.class.getSimpleName() + " has already been registered!");
            }
            Provider.provider = provider;
        }
    }
}
