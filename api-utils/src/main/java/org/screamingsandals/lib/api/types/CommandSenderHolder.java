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
 * A class holding a command sender, which can be unwrapped to a platform-specific type.
 * <p>
 * To unwrap this type to a platform-specific type, use a construction similar to the following example:
 * <pre>{@code
 *  import org.bukkit.command.CommandSender; // or other type based on the platform
 *
 *  ...
 *
 *  CommandSenderHolder commandSenderHolder = apiManager.methodReturningCommandSenderHolder();
 *  CommandSender commandSender = commandSenderHolder.as(CommandSender.class);
 *
 *  // or shortened
 *  CommandSender commandSender = apiManager.methodReturningCommandSenderHolder().as(CommandSender.class);
 * }</pre>
 * <p>
 * To create a new {@link CommandSenderHolder}, use a construction similar to the following example:
 * <pre>{@code
 *  import org.bukkit.command.CommandSender; // or other type based on the platform
 *
 *  ...
 *
 *  CommandSender commandSender = ...
 *  CommandSenderHolder commandSenderHolder = CommandSenderHolder.of(commandSender);
 * }</pre>
 * <p>
 * Currently supported platform types:
 * <ul>
 *     <li>{@code org.bukkit.command.CommandSender} and its subtypes</li>
 *     <li>{@code net.md_5.bungee.api.CommandSender} and its subtypes</li>
 *     <li>{@code com.velocitypowered.api.command.CommandSource} and its subtypes</li>
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
public interface CommandSenderHolder extends Wrapper {
    /**
     * Converts a platform-specific type to a new {@link CommandSenderHolder}. This method does not accept null.
     *
     * @param commandSender a command sender represented by a platform-specific type
     * @return new {@link CommandSenderHolder}
     * @throws IllegalArgumentException if the platform command sender object is not convertable or null has been passed
     * @throws UnsupportedOperationException if the provider has not been registered yet
     * @since 2.0.3
     */
    static @NotNull CommandSenderHolder of(@NotNull Object commandSender) {
        var result = ofNullable(commandSender);
        if (result == null) {
            throw new IllegalArgumentException("Could not wrap command sender: " + commandSender);
        }
        return result;
    }

    /**
     * Converts a platform-specific type to a new {@link CommandSenderHolder}.
     *
     * @param commandSender a command sender represented by a platform-specific type or null
     * @return new {@link CommandSenderHolder} or null if the command sender has not been passed
     * @throws IllegalArgumentException if the platform command sender object is not convertable
     * @throws UnsupportedOperationException if the provider has not been registered yet
     * @since 2.0.3
     */
    static @Nullable CommandSenderHolder ofNullable(@Nullable Object commandSender) {
        if (Provider.provider == null) {
            throw new UnsupportedOperationException("A provider for " + CommandSenderHolder.class.getSimpleName() + " has not been registered yet!");
        }
        if (commandSender == null) {
            return null;
        }
        return Provider.provider.apply(commandSender);
    }

    @ApiStatus.Internal
    final class Provider {
        private static @Nullable Function<@NotNull Object, @Nullable CommandSenderHolder> provider;

        private Provider() {
        }

        public static void registerProvider(@NotNull Function<@NotNull Object, @Nullable CommandSenderHolder> provider) {
            if (Provider.provider != null) {
                throw new UnsupportedOperationException("A provider for " + CommandSenderHolder.class.getSimpleName() + " has already been registered!");
            }
            Provider.provider = provider;
        }
    }
}
