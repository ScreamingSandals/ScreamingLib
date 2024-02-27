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
 *  import org.bukkit.inventory.ItemStack; // or other type based on the platform
 *
 *  ...
 *
 *  ItemStackHolder itemStackHolder = apiManager.methodReturningItemStackHolder();
 *  ItemStack itemStack = itemStackHolder.as(ItemStack.class);
 *
 *  // or shortened
 *  ItemStack itemStack = apiManager.methodReturningItemStackHolder().as(ItemStack.class);
 * }</pre>
 * <p>
 * To create a new {@link ItemStackHolder}, use a construction similar to the following example:
 * <pre>{@code
 *  import org.bukkit.inventory.ItemStack; // or other type based on the platform
 *
 *  ...
 *
 *  ItemStack itemStack = ...
 *  ItemStackHolder itemStackHolder = ItemStackHolder.of(itemStack);
 * }</pre>
 * <p>
 * Currently supported platform types:
 * <ul>
 *     <li>{@code org.bukkit.inventory.ItemStack} - <b>NOTE: ScreamingLib expects ItemStack to be an immutable object,
 *     please clone the object before modifying</b></li>
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
public interface ItemStackHolder extends Wrapper {
    /**
     * Converts a platform-specific object to a new {@link ItemStackHolder}. This method does not accept null.
     * <p>
     * <b>Do not modify the input ItemStack after wrapping it!</b>
     *
     * @param itemStack an item stack represented by a platform-specific object
     * @return new {@link ItemStackHolder}
     * @throws IllegalArgumentException if the platform item stack object is not convertable or null has been passed
     * @throws UnsupportedOperationException if the provider has not been registered yet
     * @since 2.0.3
     */
    static @NotNull ItemStackHolder of(@NotNull Object itemStack) {
        var result = ofNullable(itemStack);
        if (result == null) {
            throw new IllegalArgumentException("Could not wrap item stack: " + itemStack);
        }
        return result;
    }

    /**
     * Converts a platform-specific object to a new {@link ItemStackHolder}.
     * <p>
     * <b>Do not modify the input ItemStack after wrapping it!</b>
     *
     * @param itemStack an item stack represented by a platform-specific object or null
     * @return new {@link ItemStackHolder} or null if the item stack has not been passed
     * @throws IllegalArgumentException if the platform item stack object is not convertable
     * @throws UnsupportedOperationException if the provider has not been registered yet
     * @since 2.0.3
     */
    static @Nullable ItemStackHolder ofNullable(@Nullable Object itemStack) {
        if (Provider.provider == null) {
            throw new UnsupportedOperationException("A provider for " + ItemStackHolder.class.getSimpleName() + " has not been registered yet!");
        }
        if (itemStack == null) {
            return null;
        }
        return Provider.provider.apply(itemStack);
    }

    @ApiStatus.Internal
    final class Provider {
        private static @Nullable Function<@NotNull Object, @Nullable ItemStackHolder> provider;

        private Provider() {
        }

        public static void registerProvider(@NotNull Function<@NotNull Object, @Nullable ItemStackHolder> provider) {
            if (Provider.provider != null) {
                throw new UnsupportedOperationException("A provider for " + ItemStackHolder.class.getSimpleName() + " has already been registered!");
            }
            Provider.provider = provider;
        }
    }
}
