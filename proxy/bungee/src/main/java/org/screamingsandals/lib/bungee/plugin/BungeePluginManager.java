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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.plugin.PluginDescription;
import org.screamingsandals.lib.plugin.PluginKey;
import org.screamingsandals.lib.plugin.PluginManager;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.internal.InternalEarlyInitialization;

import java.util.List;
import java.util.stream.Collectors;

@Service
@InternalEarlyInitialization
public class BungeePluginManager extends PluginManager {
    @Override
    protected @Nullable Object getPlatformClass0(@NotNull PluginKey pluginKey) {
        return ProxyServer.getInstance().getPluginManager().getPlugin(pluginKey.as(String.class));
    }

    @Override
    protected boolean isEnabled0(@NotNull PluginKey pluginKey) {
        return ProxyServer.getInstance().getPluginManager().getPlugin(pluginKey.as(String.class)) != null;
    }

    @Override
    protected @Nullable PluginDescription getPlugin0(@NotNull PluginKey pluginKey) {
        var plugin = ProxyServer.getInstance().getPluginManager().getPlugin(pluginKey.as(String.class));
        if (plugin != null) {
            return wrap(plugin);
        }
        return null;
    }

    @Override
    protected @NotNull List<@NotNull PluginDescription> getAllPlugins0() {
        return ProxyServer.getInstance().getPluginManager().getPlugins().stream().map(this::wrap).collect(Collectors.toList());
    }

    @Override
    protected @Nullable PluginKey createKey0(@NotNull Object identifier) {
        return BungeePluginKey.of(identifier.toString());
    }

    @Override
    protected @NotNull PlatformType getPlatformType0() {
        return PlatformType.BUNGEE;
    }

    @Override
    protected @Nullable PluginDescription getPluginFromPlatformObject0(@NotNull Object object) {
        return ProxyServer.getInstance()
                .getPluginManager()
                .getPlugins()
                .stream()
                .filter(a -> a == object)
                .findFirst()
                .map(this::wrap)
                .orElse(null);
    }

    private @NotNull PluginDescription wrap(@NotNull Plugin plugin) {
        var description = plugin.getDescription();
        var version = description.getVersion();
        var author = description.getAuthor();
        return new PluginDescription(
                BungeePluginKey.of(description.getName()),
                description.getName(),
                version != null ? version : "unknown",
                description.getDescription(),
                author != null ? List.of(author) : List.of(),
                List.copyOf(description.getDepends()),
                List.copyOf(description.getSoftDepends()),
                plugin.getDataFolder().toPath().toAbsolutePath()
        );
    }
}
