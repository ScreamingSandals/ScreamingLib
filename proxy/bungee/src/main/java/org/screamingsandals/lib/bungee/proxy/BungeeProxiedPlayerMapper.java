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

package org.screamingsandals.lib.bungee.proxy;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bungee.proxy.listener.ChatEventListener;
import org.screamingsandals.lib.bungee.spectator.BungeeBackend;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.proxy.ProxiedPlayerMapper;
import org.screamingsandals.lib.proxy.ProxiedPlayerWrapper;
import org.screamingsandals.lib.proxy.ServerWrapper;
import org.screamingsandals.lib.spectator.Spectator;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service(dependsOn = EventManager.class)
public class BungeeProxiedPlayerMapper extends ProxiedPlayerMapper {

    public BungeeProxiedPlayerMapper(@NotNull Plugin plugin) {
        Spectator.setBackend(new BungeeBackend());
        plugin.getProxy().getPluginManager().registerListener(plugin, new ChatEventListener());
    }

    @Override
    public @Nullable ServerWrapper getServer0(@NotNull String name) {
        var serverInfo = ProxyServer.getInstance().getServerInfo(name);
        if (serverInfo != null) {
            return new BungeeServerWrapper(serverInfo);
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull List<@NotNull ServerWrapper> getServers0() {
        return ProxyServer.getInstance().getServers()
                .values()
                .stream()
                .map(BungeeServerWrapper::new)
                .collect(Collectors.toList());
    }

    @Override
    public @Nullable ProxiedPlayerWrapper getPlayer0(@NotNull String name) {
        var player = ProxyServer.getInstance().getPlayer(name);
        if (player != null) {
            return new BungeeProxiedPlayerWrapper(player);
        }
        return null;
    }

    @Override
    public @Nullable ProxiedPlayerWrapper getPlayer0(@NotNull UUID uuid) {
        var player = ProxyServer.getInstance().getPlayer(uuid);
        if (player != null) {
            return new BungeeProxiedPlayerWrapper(player);
        }
        return null;
    }

    @Override
    public @NotNull List<@NotNull ProxiedPlayerWrapper> getPlayers0() {
        return ProxyServer.getInstance().getPlayers()
                .stream()
                .map(BungeeProxiedPlayerWrapper::new)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull List<ProxiedPlayerWrapper> getPlayers0(@NotNull ServerWrapper serverWrapper) {
        return serverWrapper.as(ServerInfo.class)
                .getPlayers()
                .stream()
                .map(BungeeProxiedPlayerWrapper::new)
                .collect(Collectors.toList());
    }
}
