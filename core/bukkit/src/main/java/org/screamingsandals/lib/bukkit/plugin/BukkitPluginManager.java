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

package org.screamingsandals.lib.bukkit.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.plugin.event.PluginDisabledEventListener;
import org.screamingsandals.lib.bukkit.plugin.event.PluginEnabledEventListener;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.plugin.PluginDescription;
import org.screamingsandals.lib.plugin.PluginKey;
import org.screamingsandals.lib.plugin.PluginManager;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.internal.InternalEarlyInitialization;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@InternalEarlyInitialization
public class BukkitPluginManager extends PluginManager {
    @OnEnable
    public void onEnable(@NotNull JavaPlugin plugin) {
        new PluginEnabledEventListener(plugin);
        new PluginDisabledEventListener(plugin);
    }

    @Override
    protected @Nullable Object getPlatformClass0(@NotNull PluginKey pluginKey) {
        return Bukkit.getPluginManager().getPlugin(pluginKey.as(String.class));
    }

    @Override
    protected boolean isEnabled0(@NotNull PluginKey pluginKey) {
        return Bukkit.getPluginManager().isPluginEnabled(pluginKey.as(String.class));
    }

    @Override
    protected @Nullable PluginDescription getPlugin0(@NotNull PluginKey pluginKey) {
        var plugin = Bukkit.getPluginManager().getPlugin(pluginKey.as(String.class));
        if (plugin != null) {
            return wrap(plugin);
        }
        return null;
    }

    @Override
    protected @NotNull List<@NotNull PluginDescription> getAllPlugins0() {
        return Arrays.stream(Bukkit.getPluginManager().getPlugins()).map(this::wrap).collect(Collectors.toList());
    }

    @Override
    protected @Nullable PluginKey createKey0(@NotNull Object identifier) {
        return BukkitPluginKey.of(identifier.toString());
    }

    @Override
    protected @NotNull PlatformType getPlatformType0() {
        return PlatformType.BUKKIT;
    }

    @Override
    protected @Nullable PluginDescription getPluginFromPlatformObject0(@NotNull Object object) {
        return Arrays.stream(Bukkit.getPluginManager().getPlugins())
                .filter(a -> a == object)
                .findFirst()
                .map(this::wrap)
                .orElse(null);
    }

    private @NotNull PluginDescription wrap(@NotNull Plugin plugin) {
        var description = plugin.getDescription();
        return new PluginDescription(
                BukkitPluginKey.of(plugin.getName()),
                plugin.getName(),
                description.getVersion(),
                description.getDescription(),
                description.getAuthors(),
                description.getDepend(),
                description.getSoftDepend(),
                plugin.getDataFolder().toPath().toAbsolutePath()
        );
    }
}
