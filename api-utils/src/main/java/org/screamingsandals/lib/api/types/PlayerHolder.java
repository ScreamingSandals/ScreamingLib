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
 * A class holding a player, which can be unwrapped to a platform-specific type.
 * <p>
 * To unwrap this type to a platform-specific type, use a construction similar to the following example:
 * <pre>{@code
 *  import org.bukkit.entity.Player; // or other type based on the platform
 *
 *  ...
 *
 *  PlayerHolder playerHolder = apiManager.methodReturningPlayerHolder();
 *  Player player = playerHolder.as(Player.class);
 *
 *  // or shortened
 *  Player player = apiManager.methodReturningPlayerHolder().as(Player.class);
 * }</pre>
 * <p>
 * To create a new {@link PlayerHolder}, use a construction similar to the following example:
 * <pre>{@code
 *  import org.bukkit.entity.Player; // or other type based on the platform
 *
 *  ...
 *
 *  Player player = ...
 *  PlayerHolder playerHolder = PlayerHolder.of(player);
 * }</pre>
 * <p>
 * Currently supported platform types:
 * <ul>
 *     <li>{@code org.bukkit.entity.Player}</li>
 *     <li>{@code net.md_5.bungee.api.connection.ProxiedPlayer}</li>
 *     <li>{@code com.velocitypowered.api.proxy.Player}</li>
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
public interface PlayerHolder extends Wrapper {
    /**
     * Converts a platform-specific type to a new {@link PlayerHolder}. This method does not accept null.
     *
     * @param player a player represented by a platform-specific type
     * @return new {@link PlayerHolder}
     * @throws IllegalArgumentException if the platform player object is not convertable or null has been passed
     * @throws UnsupportedOperationException if the provider has not been registered yet
     * @since 2.0.3
     */
    static @NotNull PlayerHolder of(@NotNull Object player) {
        var result = ofNullable(player);
        if (result == null) {
            throw new IllegalArgumentException("Could not wrap player: " + player);
        }
        return result;
    }

    /**
     * Converts a platform-specific type to a new {@link PlayerHolder}.
     *
     * @param player a player represented by a platform-specific type or null
     * @return new {@link PlayerHolder} or null if the player has not been passed
     * @throws IllegalArgumentException if the platform player object is not convertable
     * @throws UnsupportedOperationException if the provider has not been registered yet
     * @since 2.0.3
     */
    static @Nullable PlayerHolder ofNullable(@Nullable Object player) {
        if (Provider.provider == null) {
            throw new UnsupportedOperationException("A provider for " + PlayerHolder.class.getSimpleName() + " has not been registered yet!");
        }
        if (player == null) {
            return null;
        }
        return Provider.provider.apply(player);
    }

    @ApiStatus.Internal
    final class Provider {
        private static @Nullable Function<@NotNull Object, @Nullable PlayerHolder> provider;

        private Provider() {
        }

        public static void registerProvider(@NotNull Function<@NotNull Object, @Nullable PlayerHolder> provider) {
            if (Provider.provider != null) {
                throw new UnsupportedOperationException("A provider for " + PlayerHolder.class.getSimpleName() + " has already been registered!");
            }
            Provider.provider = provider;
        }
    }
}
