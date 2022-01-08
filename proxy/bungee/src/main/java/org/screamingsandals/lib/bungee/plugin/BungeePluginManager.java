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

package org.screamingsandals.lib.bungee.plugin;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.plugin.PluginDescription;
import org.screamingsandals.lib.plugin.PluginKey;
import org.screamingsandals.lib.plugin.PluginManager;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.internal.InternalEarlyInitialization;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@InternalEarlyInitialization
public class BungeePluginManager extends PluginManager {
    public static void init() {
        PluginManager.init(BungeePluginManager::new);
    }

    @Override
    protected Optional<Object> getPlatformClass0(PluginKey pluginKey) {
        return Optional.ofNullable(ProxyServer.getInstance().getPluginManager().getPlugin(pluginKey.as(String.class)));
    }

    @Override
    protected boolean isEnabled0(PluginKey pluginKey) {
        return ProxyServer.getInstance().getPluginManager().getPlugin(pluginKey.as(String.class)) != null;
    }

    @Override
    protected Optional<PluginDescription> getPlugin0(PluginKey pluginKey) {
        return Optional.ofNullable(ProxyServer.getInstance().getPluginManager().getPlugin(pluginKey.as(String.class))).map(this::wrap);
    }

    @Override
    protected List<PluginDescription> getAllPlugins0() {
        return ProxyServer.getInstance().getPluginManager().getPlugins().stream().map(this::wrap).collect(Collectors.toList());
    }

    @Override
    protected Optional<PluginKey> createKey0(Object identifier) {
        return Optional.of(BungeePluginKey.of(identifier.toString()));
    }

    @Override
    protected PlatformType getPlatformType0() {
        return PlatformType.BUNGEE;
    }

    @Override
    protected Optional<PluginDescription> getPluginFromPlatformObject0(Object object) {
        return ProxyServer.getInstance()
                .getPluginManager()
                .getPlugins()
                .stream()
                .filter(a -> a == object)
                .findFirst()
                .map(this::wrap);
    }

    private PluginDescription wrap(Plugin plugin) {
        return new PluginDescription(
                BungeePluginKey.of(plugin.getDescription().getName()),
                plugin.getDescription().getName(),
                plugin.getDescription().getVersion(),
                plugin.getDescription().getDescription(),
                List.of(plugin.getDescription().getAuthor()),
                List.copyOf(plugin.getDescription().getDepends()),
                List.copyOf(plugin.getDescription().getSoftDepends()),
                plugin.getDataFolder().toPath().toAbsolutePath()
        );
    }
}
