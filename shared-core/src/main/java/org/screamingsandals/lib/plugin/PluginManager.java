/*
 * Copyright 2022 ScreamingSandals
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
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@AbstractService
public abstract class PluginManager {
    private static PluginManager pluginManager;

    @ApiStatus.Internal
    public static void init(Supplier<PluginManager> supplier) {
        if (pluginManager != null) {
            throw new UnsupportedOperationException("PluginManager is already initialized.");
        }

        pluginManager = supplier.get();
    }

    public static Optional<Object> getPlatformClass(PluginKey pluginKey) {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        return pluginManager.getPlatformClass0(pluginKey);
    }

    protected abstract Optional<Object> getPlatformClass0(PluginKey pluginKey);

    public static boolean isEnabled(PluginKey pluginKey) {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        return pluginManager.isEnabled0(pluginKey);
    }

    protected abstract boolean isEnabled0(PluginKey pluginKey);

    public static Optional<PluginDescription> getPlugin(PluginKey pluginKey) {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        return pluginManager.getPlugin0(pluginKey);
    }

    protected abstract Optional<PluginDescription> getPlugin0(PluginKey pluginKey);

    public static List<PluginDescription> getAllPlugins() {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        return pluginManager.getAllPlugins0();
    }

    protected abstract List<PluginDescription> getAllPlugins0();

    /**
     * Creates key that can be used to access plugin.
     * Any valid identifiers will create PluginKey, it doesn't matter, if the plugin really exists.
     *
     * @param identifier platform identifier
     * @return optional with PluginKey
     */
    public static Optional<PluginKey> createKey(Object identifier) {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        return pluginManager.createKey0(identifier);
    }

    protected abstract Optional<PluginKey> createKey0(Object identifier);

    public static PlatformType getPlatformType() {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        return pluginManager.getPlatformType0();
    }

    protected abstract PlatformType getPlatformType0();

    public static Optional<PluginDescription> getPluginFromPlatformObject(Object object) {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        return pluginManager.getPluginFromPlatformObject0(object);
    }

    protected abstract Optional<PluginDescription> getPluginFromPlatformObject0(Object object);
}
