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

package org.screamingsandals.lib.impl.bukkit.plugin;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.plugin.Plugins;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.plugin.Plugin;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.internal.InternalEarlyInitialization;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@InternalEarlyInitialization
public class BukkitPlugins extends Plugins {

    @Override
    protected boolean isEnabled0(@NotNull String pluginKey) {
        return Bukkit.getPluginManager().isPluginEnabled(pluginKey);
    }

    @Override
    protected @Nullable Plugin getPlugin0(@NotNull String pluginKey) {
        var plugin = Bukkit.getPluginManager().getPlugin(pluginKey);
        if (plugin != null) {
            return new BukkitPlugin(plugin);
        }
        return null;
    }

    @Override
    protected @Nullable Plugin getPluginFromPlatformObject0(@NotNull Object plugin) {
        if (plugin instanceof org.bukkit.plugin.Plugin) {
            return new BukkitPlugin((org.bukkit.plugin.Plugin) plugin);
        }
        return null;
    }

    @Override
    protected @NotNull List<@NotNull Plugin> getAllPlugins0() {
        return Arrays.stream(Bukkit.getPluginManager().getPlugins()).map(BukkitPlugin::new).collect(Collectors.toList());
    }

    @Override
    protected @NotNull PlatformType getPlatformType0() {
        return PlatformType.BUKKIT;
    }
}
