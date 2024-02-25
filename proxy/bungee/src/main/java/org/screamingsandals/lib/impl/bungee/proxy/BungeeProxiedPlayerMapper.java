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

package org.screamingsandals.lib.impl.bungee.proxy;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bungee.proxy.listener.ChatEventListener;
import org.screamingsandals.lib.impl.bungee.proxy.listener.LoginEventListener;
import org.screamingsandals.lib.impl.bungee.proxy.listener.PlayerDisconnectEventListener;
import org.screamingsandals.lib.impl.bungee.spectator.BungeeBackend;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.impl.proxy.ProxiedPlayerMapper;
import org.screamingsandals.lib.proxy.ProxiedPlayer;
import org.screamingsandals.lib.proxy.ProxiedSender;
import org.screamingsandals.lib.proxy.Server;
import org.screamingsandals.lib.impl.spectator.Spectator;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@ServiceDependencies(dependsOn = EventManager.class)
public class BungeeProxiedPlayerMapper extends ProxiedPlayerMapper {

    public BungeeProxiedPlayerMapper(@NotNull Plugin plugin) {
        Spectator.setBackend(new BungeeBackend());

        var pluginManager = plugin.getProxy().getPluginManager();
        new ChatEventListener(plugin, pluginManager);
        new LoginEventListener(plugin, pluginManager);
        new PlayerDisconnectEventListener(plugin, pluginManager);
    }

    @Override
    public @Nullable Server getServer0(@NotNull String name) {
        var serverInfo = ProxyServer.getInstance().getServerInfo(name);
        if (serverInfo != null) {
            return new BungeeServer(serverInfo);
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull List<@NotNull Server> getServers0() {
        return ProxyServer.getInstance().getServers()
                .values()
                .stream()
                .map(BungeeServer::new)
                .collect(Collectors.toList());
    }

    @Override
    public @Nullable ProxiedPlayer getPlayer0(@NotNull String name) {
        var player = ProxyServer.getInstance().getPlayer(name);
        if (player != null) {
            return new BungeeProxiedPlayer(player);
        }
        return null;
    }

    @Override
    public @Nullable ProxiedPlayer getPlayer0(@NotNull UUID uuid) {
        var player = ProxyServer.getInstance().getPlayer(uuid);
        if (player != null) {
            return new BungeeProxiedPlayer(player);
        }
        return null;
    }

    @Override
    public @NotNull List<@NotNull ProxiedPlayer> getPlayers0() {
        return ProxyServer.getInstance().getPlayers()
                .stream()
                .map(BungeeProxiedPlayer::new)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull List<ProxiedPlayer> getPlayers0(@NotNull Server serverWrapper) {
        return serverWrapper.as(ServerInfo.class)
                .getPlayers()
                .stream()
                .map(BungeeProxiedPlayer::new)
                .collect(Collectors.toList());
    }

    @Override
    protected @NotNull ProxiedSender senderFromPlatform(@NotNull Object platformObject) {
        if (platformObject instanceof ProxiedSender) {
            return (ProxiedSender) platformObject;
        } else if (platformObject instanceof net.md_5.bungee.api.connection.ProxiedPlayer) {
            return new BungeeProxiedPlayer((net.md_5.bungee.api.connection.ProxiedPlayer) platformObject);
        } else if (platformObject instanceof CommandSender) {
            return new BungeeProxiedSender((CommandSender) platformObject);
        }
        throw new IllegalArgumentException("Not possible to convert unknown object type to ProxiedSender: " + platformObject);
    }

    @Override
    protected @NotNull ProxiedPlayer playerFromPlatform(@NotNull Object platformObject) {
        if (platformObject instanceof ProxiedPlayer) {
            return (ProxiedPlayer) platformObject;
        } else if (platformObject instanceof net.md_5.bungee.api.connection.ProxiedPlayer) {
            return new BungeeProxiedPlayer((net.md_5.bungee.api.connection.ProxiedPlayer) platformObject);
        }
        throw new IllegalArgumentException("Not possible to convert unknown object type to ProxiedPlayer: " + platformObject);
    }
}
