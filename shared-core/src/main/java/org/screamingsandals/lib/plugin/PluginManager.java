/*
 * Copyright 2023 ScreamingSandals
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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.List;

@AbstractService
public abstract class PluginManager {
    private static @Nullable PluginManager pluginManager;

    @ApiStatus.Internal
    public PluginManager() {
        if (pluginManager != null) {
            throw new UnsupportedOperationException("PluginManager is already initialized.");
        }

        pluginManager = this;
    }

    public static @Nullable Object getPlatformClass(@NotNull PluginKey pluginKey) {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        return pluginManager.getPlatformClass0(pluginKey);
    }

    protected abstract @Nullable Object getPlatformClass0(@NotNull PluginKey pluginKey);

    public static boolean isEnabled(@NotNull PluginKey pluginKey) {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        return pluginManager.isEnabled0(pluginKey);
    }

    protected abstract boolean isEnabled0(@NotNull PluginKey pluginKey);

    public static @Nullable PluginDescription getPlugin(@NotNull PluginKey pluginKey) {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        return pluginManager.getPlugin0(pluginKey);
    }

    protected abstract @Nullable PluginDescription getPlugin0(@NotNull PluginKey pluginKey);

    public static @NotNull List<@NotNull PluginDescription> getAllPlugins() {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        return pluginManager.getAllPlugins0();
    }

    protected abstract @NotNull List<@NotNull PluginDescription> getAllPlugins0();

    /**
     * Creates key that can be used to access plugin.
     * Any valid identifiers will create PluginKey, it doesn't matter, if the plugin really exists.
     *
     * @param identifier platform identifier
     * @return PluginKey or null if identifier is invalid
     */
    public static @Nullable PluginKey createKey(@NotNull Object identifier) {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        return pluginManager.createKey0(identifier);
    }

    protected abstract @Nullable PluginKey createKey0(@NotNull Object identifier);

    public static @NotNull PlatformType getPlatformType() {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        return pluginManager.getPlatformType0();
    }

    protected abstract @NotNull PlatformType getPlatformType0();

    @Contract("null -> null")
    public static @Nullable PluginDescription getPluginFromPlatformObject(@Nullable Object object) {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        if (object == null) {
            return null;
        }
        return pluginManager.getPluginFromPlatformObject0(object);
    }

    protected abstract @Nullable PluginDescription getPluginFromPlatformObject0(@NotNull Object object);
}
