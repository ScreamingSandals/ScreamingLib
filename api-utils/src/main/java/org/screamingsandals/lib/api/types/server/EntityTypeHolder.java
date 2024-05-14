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
 *  import org.bukkit.entity.EntityType; // or other type based on the platform
 *
 *  ...
 *
 *  EntityTypeHolder entityTypeHolder = apiManager.methodReturningEntityTypeHolder();
 *  EntityType entityType = entityTypeHolder.as(EntityType.class);
 *
 *  // or shortened
 *  EntityType entityType = apiManager.methodReturningEntityTypeHolder().as(EntityType.class);
 * }</pre>
 * <p>
 * To create a new {@link EntityTypeHolder}, use a construction similar to the following example:
 * <pre>{@code
 *  import org.bukkit.entity.EntityType; // or other type based on the platform
 *
 *  ...
 *
 *  EntityType entityType = ...
 *  EntityTypeHolder entityTypeHolder = EntityTypeHolder.of(entityType);
 * }</pre>
 * <p>
 * Currently supported platform types:
 * <ul>
 *     <li>{@code org.bukkit.entity.EntityType} - for legacy versions (<= 1.11.2), unwrapping to this class can be
 *     inaccurate and may cause unexpected type conversion for horses, skeleton types and zombie types (e.g. skeleton_horse -> horse)</li>
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
public interface EntityTypeHolder extends Wrapper {
    /**
     * Converts a platform-specific type to a new {@link EntityTypeHolder}. This method does not accept null.
     *
     * @param entityType an entity type represented by a platform-specific type
     * @return new {@link EntityTypeHolder}
     * @throws IllegalArgumentException if the platform entity type object is not convertable or null has been passed
     * @throws UnsupportedOperationException if the provider has not been registered yet
     * @since 2.0.3
     */
    static @NotNull EntityTypeHolder of(@NotNull Object entityType) {
        var result = ofNullable(entityType);
        if (result == null) {
            throw new IllegalArgumentException("Could not wrap entity type: " + entityType);
        }
        return result;
    }

    /**
     * Converts a platform-specific type to a new {@link EntityTypeHolder}.
     *
     * @param entityType an entity type represented by a platform-specific type or null
     * @return new {@link EntityTypeHolder} or null if the entity type has not been passed
     * @throws IllegalArgumentException if the platform entity type object is not convertable
     * @throws UnsupportedOperationException if the provider has not been registered yet
     * @since 2.0.3
     */
    static @Nullable EntityTypeHolder ofNullable(@Nullable Object entityType) {
        if (Provider.provider == null) {
            throw new UnsupportedOperationException("A provider for " + EntityTypeHolder.class.getSimpleName() + " has not been registered yet!");
        }
        if (entityType == null) {
            return null;
        }
        return Provider.provider.apply(entityType);
    }

    @ApiStatus.Internal
    final class Provider {
        private static @Nullable Function<@NotNull Object, @Nullable EntityTypeHolder> provider;

        private Provider() {
        }

        public static void registerProvider(@NotNull Function<@NotNull Object, @Nullable EntityTypeHolder> provider) {
            if (Provider.provider != null) {
                throw new UnsupportedOperationException("A provider for " + EntityTypeHolder.class.getSimpleName() + " has already been registered!");
            }
            Provider.provider = provider;
        }
    }
}
