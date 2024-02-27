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
 * A class holding an entity type, which can be unwrapped to a platform-specific type.
 * <p>
 * To unwrap this type to a platform-specific type, use a construction similar to the following example:
 * <pre>{@code
 *  import org.bukkit.entity.Entity; // or other type based on the platform
 *
 *  ...
 *
 *  EntityHolder entityHolder = apiManager.methodReturningEntityTypeHolder();
 *  Entity entity = entityHolder.as(Entity.class);
 *
 *  // or shortened
 *  Entity entity = apiManager.methodReturningEntityHolder().as(Entity.class);
 * }</pre>
 * <p>
 * To create a new {@link EntityHolder}, use a construction similar to the following example:
 * <pre>{@code
 *  import org.bukkit.entity.Entity; // or other type based on the platform
 *
 *  ...
 *
 *  Entity entity = ...
 *  EntityHolder entityHolder = EntityHolder.of(entity);
 * }</pre>
 * <p>
 * Currently supported platform types:
 * <ul>
 *     <li>{@code org.bukkit.entity.Entity} and its subtypes</li>
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
public interface EntityHolder extends Wrapper {
    /**
     * Converts a platform-specific object to a new {@link EntityHolder}. This method does not accept null.
     *
     * @param entity an entity represented by a platform-specific object
     * @return new {@link EntityHolder}
     * @throws IllegalArgumentException if the platform entity object is not convertable or null has been passed
     * @throws UnsupportedOperationException if the provider has not been registered yet
     * @since 2.0.3
     */
    static @NotNull EntityHolder of(@NotNull Object entity) {
        var result = ofNullable(entity);
        if (result == null) {
            throw new IllegalArgumentException("Could not wrap entity: " + entity);
        }
        return result;
    }

    /**
     * Converts a platform-specific object to a new {@link EntityHolder}.
     *
     * @param entity an entity represented by a platform-specific object or null
     * @return new {@link EntityHolder} or null if the entity has not been passed
     * @throws IllegalArgumentException if the platform entity object is not convertable
     * @throws UnsupportedOperationException if the provider has not been registered yet
     * @since 2.0.3
     */
    static @Nullable EntityHolder ofNullable(@Nullable Object entity) {
        if (Provider.provider == null) {
            throw new UnsupportedOperationException("A provider for " + EntityHolder.class.getSimpleName() + " has not been registered yet!");
        }
        if (entity == null) {
            return null;
        }
        return Provider.provider.apply(entity);
    }

    @ApiStatus.Internal
    final class Provider {
        private static @Nullable Function<@NotNull Object, @Nullable EntityHolder> provider;

        private Provider() {
        }

        public static void registerProvider(@NotNull Function<@NotNull Object, @Nullable EntityHolder> provider) {
            if (Provider.provider != null) {
                throw new UnsupportedOperationException("A provider for " + EntityHolder.class.getSimpleName() + " has already been registered!");
            }
            Provider.provider = provider;
        }
    }
}
