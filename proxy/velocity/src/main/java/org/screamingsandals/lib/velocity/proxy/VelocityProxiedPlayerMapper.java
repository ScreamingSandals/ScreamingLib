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

package org.screamingsandals.lib.velocity.proxy;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.adventure.spectator.audience.adapter.AdventureAdapter;
import org.screamingsandals.lib.adventure.spectator.audience.adapter.AdventurePlayerAdapter;
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
import org.screamingsandals.lib.velocity.proxy.event.ChatEventHandlerFactory;
import org.screamingsandals.lib.velocity.proxy.event.PlayerLeaveEventFactory;
import org.screamingsandals.lib.velocity.proxy.event.PlayerLoginEventFactory;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service(dependsOn = {
        EventManager.class
})
public class VelocityProxiedPlayerMapper extends ProxiedPlayerMapper {
    private static final @NotNull String CONSOLE_NAME = "CONSOLE";
    private final @NotNull Object plugin;
    private final @NotNull ProxyServer proxyServer;

    public VelocityProxiedPlayerMapper(@NotNull Object plugin, @NotNull ProxyServer proxyServer) {
        this.plugin = plugin;
        this.proxyServer = proxyServer;
        var backend = new AdventureBackend();
        registerEvents();
        Spectator.setBackend(backend);

        playerConverter
                .registerP2W(Player.class, player -> new ProxiedPlayerWrapper(player.getUsername(), player.getUniqueId()))
                .registerW2P(Player.class, proxiedPlayerWrapper -> this.proxyServer.getPlayer(proxiedPlayerWrapper.getUuid()).orElse(null));

        senderConverter
                .registerP2W(CommandSource.class, source -> {
                    if (source instanceof Player) {
                        final var casted = (Player) source;
                        return new ProxiedPlayerWrapper(casted.getUsername(), casted.getUniqueId());
                    }
                    return new ProxiedSenderWrapper(CONSOLE_NAME, ProxiedSenderWrapper.Type.CONSOLE);
                })
                .registerW2P(CommandSource.class, proxiedPlayerWrapper -> {
                    if (proxiedPlayerWrapper.getType() == ProxiedSenderWrapper.Type.CONSOLE) {
                        return proxyServer.getConsoleCommandSource();
                    }

                    return proxyServer.getPlayer(proxiedPlayerWrapper.getName()).orElse(null);
                })
                .registerW2P(ProxiedPlayerWrapper.class, wrapper -> {
                   if (wrapper.getType() == ProxiedSenderWrapper.Type.CONSOLE) {
                       return null;
                   }

                   final var player = proxyServer.getPlayer(wrapper.getName()).orElse(null);
                   if (player == null) {
                       return null;
                   }

                   return wrapPlayer(player);
                });

        serverConverter
                .registerP2W(ServerInfo.class, serverInfo -> new ServerWrapper(serverInfo.getName(), serverInfo.getAddress()))
                .registerP2W(RegisteredServer.class, registeredServer -> new ServerWrapper(registeredServer.getServerInfo().getName(), registeredServer.getServerInfo().getAddress()))
                .registerW2P(ServerInfo.class, serverWrapper -> this.proxyServer.getServer(serverWrapper.getName()).map(RegisteredServer::getServerInfo).orElse(null))
                .registerW2P(RegisteredServer.class, serverWrapper -> this.proxyServer.getServer(serverWrapper.getName()).orElse(null));
    }

    @Override
    public void sendMessage0(@NotNull ProxiedSenderWrapper wrapper, @NotNull String message) {
        wrapper.as(CommandSource.class).sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
    }

    @Override
    public void switchServer0(@NotNull ProxiedPlayerWrapper playerWrapper, @NotNull ServerWrapper server) {
        playerWrapper.as(Player.class).createConnectionRequest(server.as(RegisteredServer.class));
    }

    @Override
    public @Nullable ServerWrapper getServer0(@NotNull String name) {
        return proxyServer.getServer(name).map(ProxiedPlayerMapper::wrapServer).orElse(null);
    }

    @Override
    public @NotNull List<@NotNull ServerWrapper> getServers0() {
        return proxyServer.getAllServers().stream().map(ProxiedPlayerMapper::wrapServer).collect(Collectors.toList());
    }

    @Override
    public @Nullable ProxiedPlayerWrapper getPlayer0(@NotNull String name) {
        return proxyServer.getPlayer(name).map(ProxiedPlayerMapper::wrapPlayer).orElse(null);
    }

    @Override
    public @Nullable ProxiedPlayerWrapper getPlayer0(@NotNull UUID uuid) {
        return proxyServer.getPlayer(uuid).map(ProxiedPlayerMapper::wrapPlayer).orElse(null);
    }

    @Override
    public @NotNull List<@NotNull ProxiedPlayerWrapper> getPlayers0() {
        return proxyServer.getAllPlayers().stream().map(ProxiedPlayerMapper::wrapPlayer).collect(Collectors.toList());
    }

    @Override
    public @NotNull List<@NotNull ProxiedPlayerWrapper> getPlayers0(@NotNull ServerWrapper serverWrapper) {
        return serverWrapper.as(RegisteredServer.class).getPlayersConnected().stream().map(ProxiedPlayerMapper::wrapPlayer).collect(Collectors.toList());
    }

    @Override
    public boolean hasPermission0(@NotNull CommandSenderWrapper wrapper, @NotNull Permission permission) {
        if (permission instanceof SimplePermission) {
            if (isPermissionSet0(wrapper, permission)) {
                return wrapper.as(CommandSource.class).hasPermission(((SimplePermission) permission).getPermissionString());
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
    public boolean isPermissionSet0(@NotNull CommandSenderWrapper wrapper, @NotNull Permission permission) {
        if (permission instanceof SimplePermission) {
            return wrapper.as(CommandSource.class).getPermissionValue(((SimplePermission) permission).getPermissionString()) == Tristate.UNDEFINED;
        }
        return true;
    }

    @Override
    public @NotNull Locale getLocale0(@NotNull ProxiedSenderWrapper wrapper) {
        return wrapper.asOptional(Player.class)
                .map(player -> player.getPlayerSettings().getLocale())
                .orElse(Locale.US);
    }

    @Override
    protected @NotNull Adapter adapter0(@NotNull ProxiedSenderWrapper wrapper) {
        var source = wrapper.as(CommandSource.class);
        if (source instanceof Player && source instanceof ProxiedPlayerWrapper) {
            return new AdventurePlayerAdapter(source, (PlayerAudience) wrapper);
        /*} else if (source instanceof ConsoleCommandSource && TODO) {
            return new AdventureConsoleAdapter(source, (ConsoleAudience) wrapper);*/
        }
        return new AdventureAdapter(source, wrapper);
    }

    private void registerEvents() {
        new ChatEventHandlerFactory(plugin, proxyServer);
        new PlayerLoginEventFactory(plugin, proxyServer);
        new PlayerLeaveEventFactory(plugin, proxyServer);
    }
}
