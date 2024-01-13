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

package org.screamingsandals.lib.plugin;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.List;

@AbstractService("org.screamingsandals.lib.impl.{platform}.plugin.{Platform}Plugins")
public abstract class Plugins {
    private static @Nullable Plugins pluginManager;

    @ApiStatus.Internal
    public Plugins() {
        if (pluginManager != null) {
            throw new UnsupportedOperationException("PluginManager is already initialized.");
        }

        pluginManager = this;
    }

    public static boolean isEnabled(@NotNull String pluginKey) {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        return pluginManager.isEnabled0(pluginKey);
    }

    protected abstract boolean isEnabled0(@NotNull String pluginKey);

    public static @Nullable Plugin getPlugin(@NotNull String pluginKey) {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        return pluginManager.getPlugin0(pluginKey);
    }

    protected abstract @Nullable Plugin getPlugin0(@NotNull String pluginKey);

    public static @Nullable Plugin getPluginFromPlatformObject(@NotNull Object plugin) {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        return pluginManager.getPluginFromPlatformObject0(plugin);
    }

    protected abstract @Nullable Plugin getPluginFromPlatformObject0(@NotNull Object plugin);

    public static @NotNull List<@NotNull Plugin> getAllPlugins() {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        return pluginManager.getAllPlugins0();
    }

    protected abstract @NotNull List<@NotNull Plugin> getAllPlugins0();

    public static @NotNull PlatformType getPlatformType() {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        return pluginManager.getPlatformType0();
    }

    protected abstract @NotNull PlatformType getPlatformType0();
}
