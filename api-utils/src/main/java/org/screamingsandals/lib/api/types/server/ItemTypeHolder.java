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
 * A class holding an item type, which can be unwrapped to a platform-specific type.
 * <p>
 * To unwrap this type to a platform-specific type, use a construction similar to the following example:
 * <pre>{@code
 *  import org.bukkit.Material; // or other type based on the platform
 *
 *  ...
 *
 *  ItemTypeHolder itemTypeHolder = apiManager.methodReturningItemType();
 *  Material material = itemTypeHolder.as(Material.class);
 *
 *  // or shortened
 *  Material material = apiManager.methodReturningItemType().as(Material.class);
 * }</pre>
 * <p>
 * To create a new {@link ItemTypeHolder}, use a construction similar to the following example:
 * <pre>{@code
 *  import org.bukkit.Material; // or other type based on the platform
 *
 *  ...
 *
 *  Material material = ...
 *  ItemTypeHolder itemTypeHolder = ItemTypeHolder.of(material);
 * }</pre>
 * <p>
 * Currently supported platform types:
 * <ul>
 *     <li>{@code org.bukkit.Material} - for legacy versions (<= 1.12.2), unwrapping to this class can be
 *     inaccurate and may cause unexpected type conversion (e.g. diorite -> stone)</li>
 *     <li>{@code org.bukkit.inventory.ItemStack}</li>
 * </ul>
 * <p>
 * NOTE: The type should be directly used only when interacting with an API of a ScreamingLib-based plugin.
 * The ScreamingLib-base plugin itself should use the actual Component type provided by the library.
 * <p>
 * The holder itself lacks identity and should not be compared using {@code ==}, use {@link Object#equals(Object)} instead.
 *
 * @since 2.0.3
 */
@ApiStatus.NonExtendable
public interface ItemTypeHolder extends Wrapper {
    /**
     * Converts a platform-specific type to a new {@link ItemTypeHolder}. This method does not accept null.
     *
     * @param itemType an item type represented by a platform-specific type
     * @return new {@link ItemTypeHolder}
     * @throws IllegalArgumentException if the platform item type object is not convertable or null has been passed
     * @throws UnsupportedOperationException if the provider has not been registered yet
     * @since 2.0.3
     */
    static @NotNull ItemTypeHolder of(@NotNull Object itemType) {
        var result = ofNullable(itemType);
        if (result == null) {
            throw new IllegalArgumentException("Could not wrap item type: " + itemType);
        }
        return result;
    }

    /**
     * Converts a platform-specific type to a new {@link ItemTypeHolder}.
     *
     * @param itemType an item type represented by a platform-specific type or null
     * @return new {@link ItemTypeHolder} or null if the item type has not been passed
     * @throws IllegalArgumentException if the platform item type object is not convertable
     * @throws UnsupportedOperationException if the provider has not been registered yet
     * @since 2.0.3
     */
    static @Nullable ItemTypeHolder ofNullable(@Nullable Object itemType) {
        if (Provider.provider == null) {
            throw new UnsupportedOperationException("A provider for " + ItemTypeHolder.class.getSimpleName() + " has not been registered yet!");
        }
        if (itemType == null) {
            return null;
        }
        return Provider.provider.apply(itemType);
    }

    @ApiStatus.Internal
    final class Provider {
        private static @Nullable Function<@NotNull Object, @Nullable ItemTypeHolder> provider;

        private Provider() {
        }

        public static void registerProvider(@NotNull Function<@NotNull Object, @Nullable ItemTypeHolder> provider) {
            if (Provider.provider != null) {
                throw new UnsupportedOperationException("A provider for " + ItemTypeHolder.class.getSimpleName() + " has already been registered!");
            }
            Provider.provider = provider;
        }
    }
}
