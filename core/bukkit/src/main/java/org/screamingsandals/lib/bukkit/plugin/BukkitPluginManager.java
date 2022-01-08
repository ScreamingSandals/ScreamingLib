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
import org.screamingsandals.lib.bukkit.plugin.event.PluginDisabledEventListener;
import org.screamingsandals.lib.bukkit.plugin.event.PluginEnabledEventListener;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.plugin.PluginDescription;
import org.screamingsandals.lib.plugin.PluginKey;
import org.screamingsandals.lib.plugin.PluginManager;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.internal.InternalEarlyInitialization;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@InternalEarlyInitialization
public class BukkitPluginManager extends PluginManager {
    public static void init(JavaPlugin plugin, Controllable controllable) {
        PluginManager.init(BukkitPluginManager::new);
        controllable.enable(() -> {
            new PluginEnabledEventListener(plugin);
            new PluginDisabledEventListener(plugin);
        });
    }

    @Override
    protected Optional<Object> getPlatformClass0(PluginKey pluginKey) {
        return Optional.ofNullable(Bukkit.getPluginManager().getPlugin(pluginKey.as(String.class)));
    }

    @Override
    protected boolean isEnabled0(PluginKey pluginKey) {
        return Bukkit.getPluginManager().isPluginEnabled(pluginKey.as(String.class));
    }

    @Override
    protected Optional<PluginDescription> getPlugin0(PluginKey pluginKey) {
        return Optional.ofNullable(Bukkit.getPluginManager().getPlugin(pluginKey.as(String.class))).map(this::wrap);
    }

    @Override
    protected List<PluginDescription> getAllPlugins0() {
        return Arrays.stream(Bukkit.getPluginManager().getPlugins()).map(this::wrap).collect(Collectors.toList());
    }

    @Override
    protected Optional<PluginKey> createKey0(Object identifier) {
        return Optional.of(BukkitPluginKey.of(identifier.toString()));
    }

    @Override
    protected PlatformType getPlatformType0() {
        return PlatformType.BUKKIT;
    }

    @Override
    protected Optional<PluginDescription> getPluginFromPlatformObject0(Object object) {
        return Arrays.stream(Bukkit.getPluginManager().getPlugins())
                .filter(a -> a == object)
                .findFirst()
                .map(this::wrap);
    }

    private PluginDescription wrap(Plugin plugin) {
        return new PluginDescription(
                BukkitPluginKey.of(plugin.getName()),
                plugin.getName(),
                plugin.getDescription().getVersion(),
                plugin.getDescription().getDescription(),
                plugin.getDescription().getAuthors(),
                plugin.getDescription().getDepend(),
                plugin.getDescription().getSoftDepend(),
                plugin.getDataFolder().toPath().toAbsolutePath()
        );
    }
}
