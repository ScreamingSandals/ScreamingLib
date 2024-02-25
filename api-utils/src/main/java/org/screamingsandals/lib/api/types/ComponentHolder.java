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

package org.screamingsandals.lib.api.types;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.api.Wrapper;

import java.util.function.Function;

/**
 * A class holding a text component, which can be unwrapped to a platform-specific type.
 * <p>
 * To unwrap this type to a platform-specific type, use a construction similar to the following example:
 * <pre>{@code
 *  import net.kyori.adventure.text.Component; // or other type based on the platform
 *
 *  ...
 *
 *  ComponentHolder componentHolder = apiManager.methodReturningComponentHolder();
 *  Component component = componentHolder.as(Component.class);
 *
 *  // or shortened
 *  Component component = apiManager.methodReturningComponentHolder().as(Component.class);
 * }</pre>
 * <p>
 * To create a new {@link ComponentHolder}, use a construction similar to the following example:
 * <pre>{@code
 *  import net.kyori.adventure.text.Component; // or other type based on the platform
 *
 *  ...
 *
 *  Component component = ...
 *  ComponentHolder componentHolder = ComponentHolder.of(component);
 * }</pre>
 * <p>
 * Currently supported platform types:
 * <ul>
 *     <li>{@code net.kyori.adventure.text.Component} and its subtypes</li>
 *     <li>{@code net.md_5.bungee.api.chat.BaseComponent} and its subtypes</li>
 * </ul>
 * <p>
 * NOTE: The type should be directly used only when interacting with an API of a ScreamingLib-based plugin.
 * The ScreamingLib-base plugin itself should use the actual Component type provided by the library.
 * <p>
 * This object lacks identity and should not be compared using {@code ==}, use {@link Object#equals(Object)} instead.
 *
 * @since 2.0.3
 */
@ApiStatus.NonExtendable
public interface ComponentHolder extends Wrapper {
    /**
     * Converts a platform-specific type to a new {@link ComponentHolder}. This method does not accept null.
     *
     * @param component a component represented by a platform-specific type
     * @return new {@link ComponentHolder}
     * @throws IllegalArgumentException if the platform component object is not convertable or null has been passed
     * @throws UnsupportedOperationException if the provider has not been registered yet
     * @since 2.0.3
     */
    static @NotNull ComponentHolder of(@NotNull Object component) {
        var result = ofNullable(component);
        if (result == null) {
            throw new IllegalArgumentException("Could not wrap component: " + component);
        }
        return result;
    }

    /**
     * Converts a platform-specific type to a new {@link ComponentHolder}.
     *
     * @param component a component represented by a platform-specific type or null
     * @return new {@link ComponentHolder} or null if the component has not been passed
     * @throws IllegalArgumentException if the platform component object is not convertable
     * @throws UnsupportedOperationException if the provider has not been registered yet
     * @since 2.0.3
     */
    static @Nullable ComponentHolder ofNullable(@Nullable Object component) {
        if (Provider.provider == null) {
            throw new UnsupportedOperationException("A provider for " + ComponentHolder.class.getSimpleName() + " has not been registered yet!");
        }
        if (component == null) {
            return null;
        }
        return Provider.provider.apply(component);
    }

    @ApiStatus.Internal
    final class Provider {
        private static @Nullable Function<@NotNull Object, @Nullable ComponentHolder> provider;

        private Provider() {
        }

        public static void registerProvider(@NotNull Function<@NotNull Object, @Nullable ComponentHolder> provider) {
            if (Provider.provider != null) {
                throw new UnsupportedOperationException("A provider for " + ComponentHolder.class.getSimpleName() + " has already been registered!");
            }
            Provider.provider = provider;
        }
    }
}
