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

package org.screamingsandals.lib.placeholders;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.placeholders.hooks.Hook;
import org.screamingsandals.lib.sender.MultiPlatformOfflinePlayer;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.LinkedList;
import java.util.List;

@AbstractService("org.screamingsandals.lib.impl.{platform}.placeholders.{Platform}PlaceholderManager")
public abstract class PlaceholderManager {

    protected final @NotNull List<@NotNull Hook> activeHooks = new LinkedList<>();

    private static @Nullable PlaceholderManager placeholderManager;

    @ApiStatus.Internal
    public PlaceholderManager() {
        if (placeholderManager != null) {
            throw new UnsupportedOperationException("PlaceholderManager is already initialized!");
        }
        placeholderManager = this;
    }

    public static void registerExpansion(@NotNull PlaceholderExpansion expansion) {
        if (placeholderManager == null) {
            throw new UnsupportedOperationException("PlaceholderManager is not initialized yet!");
        }
        placeholderManager.registerExpansion0(expansion);
    }

    public abstract void registerExpansion0(@NotNull PlaceholderExpansion expansion);

    public static @NotNull String resolveString(@Nullable MultiPlatformOfflinePlayer player, @NotNull String message) {
        if (placeholderManager == null) {
            throw new UnsupportedOperationException("PlaceholderManager is not initialized yet!");
        }
        return placeholderManager.resolveString0(player, message);
    }

    public abstract @NotNull String resolveString0(@Nullable MultiPlatformOfflinePlayer player, @NotNull String message);

    public static boolean isInitialized() {
        return placeholderManager != null;
    }
}
