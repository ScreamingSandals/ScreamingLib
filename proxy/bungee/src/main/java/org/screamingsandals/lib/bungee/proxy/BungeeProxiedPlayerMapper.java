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

package org.screamingsandals.lib.bungee.proxy;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Plugin;
import org.screamingsandals.lib.bungee.proxy.listener.ChatEventListener;
import org.screamingsandals.lib.bungee.spectator.BungeeBackend;
import org.screamingsandals.lib.bungee.spectator.audience.adapter.BungeeAdapter;
import org.screamingsandals.lib.bungee.spectator.audience.adapter.BungeePlayerAdapter;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.proxy.ProxiedPlayerMapper;
import org.screamingsandals.lib.proxy.ProxiedPlayerWrapper;
import org.screamingsandals.lib.proxy.ProxiedSenderWrapper;
import org.screamingsandals.lib.proxy.ServerWrapper;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.permissions.*;
import org.screamingsandals.lib.spectator.Spectator;
import org.screamingsandals.lib.spectator.audience.PlayerAudience;
import org.screamingsandals.lib.spectator.audience.adapter.Adapter;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service(dependsOn = {
        EventManager.class
})
public class BungeeProxiedPlayerMapper extends ProxiedPlayerMapper {

    public static void init(Plugin plugin) {
        ProxiedPlayerMapper.init(() -> new BungeeProxiedPlayerMapper(plugin));
    }

    public BungeeProxiedPlayerMapper(Plugin plugin) {
        Spectator.setBackend(new BungeeBackend());
        plugin.getProxy().getPluginManager().registerListener(plugin, new ChatEventListener());

        playerConverter
                .registerP2W(ProxiedPlayer.class, proxiedPlayer -> new ProxiedPlayerWrapper(proxiedPlayer.getName(), proxiedPlayer.getUniqueId()))
                .registerW2P(ProxiedPlayer.class, proxiedPlayerWrapper -> ProxyServer.getInstance().getPlayer(proxiedPlayerWrapper.getUuid()));

        serverConverter
                .registerP2W(Server.class, server -> new ServerWrapper(server.getInfo().getName(), server.getInfo().getSocketAddress()))
                .registerP2W(ServerInfo.class, serverInfo -> new ServerWrapper(serverInfo.getName(), serverInfo.getSocketAddress()))
                .registerW2P(ServerInfo.class, serverWrapper -> ProxyServer.getInstance().getServerInfo(serverWrapper.getName()));
    }

    @Override
    public void sendMessage0(ProxiedSenderWrapper wrapper, String message) {
        wrapper.as(CommandSender.class).sendMessage(TextComponent.fromLegacyText(message));
    }

    @Override
    public void switchServer0(ProxiedPlayerWrapper playerWrapper, ServerWrapper server) {
        playerWrapper.as(ProxiedPlayer.class).connect(server.as(ServerInfo.class));
    }

    @Override
    public Optional<ServerWrapper> getServer0(String name) {
        return Optional.ofNullable(ProxyServer.getInstance()
                .getServerInfo(name))
                .map(ProxiedPlayerMapper::wrapServer);
    }

    @SuppressWarnings("deprecation")
    @Override
    public List<ServerWrapper> getServers0() {
        return ProxyServer.getInstance().getServers()
                .values()
                .stream()
                .map(ProxiedPlayerMapper::wrapServer)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProxiedPlayerWrapper> getPlayer0(String name) {
        return Optional.ofNullable(ProxyServer.getInstance().getPlayer(name))
                .map(ProxiedPlayerMapper::wrapPlayer);
    }

    @Override
    public Optional<ProxiedPlayerWrapper> getPlayer0(UUID uuid) {
        return Optional.ofNullable(ProxyServer.getInstance().getPlayer(uuid))
                .map(ProxiedPlayerMapper::wrapPlayer);
    }

    @Override
    public List<ProxiedPlayerWrapper> getPlayers0() {
        return ProxyServer.getInstance().getPlayers()
                .stream()
                .map(ProxiedPlayerMapper::wrapPlayer)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProxiedPlayerWrapper> getPlayers0(ServerWrapper serverWrapper) {
        return serverWrapper.as(ServerInfo.class)
                .getPlayers()
                .stream()
                .map(ProxiedPlayerMapper::wrapPlayer)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasPermission0(CommandSenderWrapper wrapper, Permission permission) {
        if (permission instanceof SimplePermission) {
            if (isPermissionSet0(wrapper, permission)) {
                return wrapper.as(CommandSender.class).hasPermission(((SimplePermission) permission).getPermissionString());
            } else {
                return ((SimplePermission) permission).isDefaultAllowed();
            }
        } else if (permission instanceof AndPermission) {
            return ((AndPermission) permission).getPermissions().stream().allMatch(permission1 -> hasPermission0(wrapper, permission1));
        } else if (permission instanceof OrPermission) {
            return ((OrPermission) permission).getPermissions().stream().anyMatch(permission1 -> hasPermission0(wrapper, permission1));
        } else if (permission instanceof PredicatePermission) {
            return permission.hasPermission(wrapper);
        }
        return false;
    }

    @Override
    public boolean isPermissionSet0(CommandSenderWrapper wrapper, Permission permission) {
        if (permission instanceof SimplePermission) {
            return wrapper.as(CommandSender.class).getPermissions().contains(((SimplePermission) permission).getPermissionString());
        }
        return true;
    }

    @Override
    public Locale getLocale0(ProxiedSenderWrapper wrapper) {
        return wrapper.asOptional(ProxiedPlayer.class)
                .map(ProxiedPlayer::getLocale)
                .orElse(Locale.US);
    }

    @Override
    protected Adapter adapter0(ProxiedSenderWrapper wrapper) {
        var source = wrapper.as(CommandSender.class);
        if (source instanceof ProxiedPlayer && source instanceof ProxiedPlayerWrapper) {
            return new BungeePlayerAdapter((ProxiedPlayer) source, (PlayerAudience) wrapper);
        /*} else if (source instanceof ConsoleCommandSource && TODO) {
            return new BungeeConsoleAudience(source, (ConsoleAudience) wrapper);*/
        }
        return new BungeeAdapter(source, wrapper);
    }
}
