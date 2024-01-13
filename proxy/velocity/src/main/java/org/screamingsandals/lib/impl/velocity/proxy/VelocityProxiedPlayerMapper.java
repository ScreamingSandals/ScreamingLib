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

package org.screamingsandals.lib.impl.velocity.proxy;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.impl.velocity.proxy.event.ChatEventHandlerFactory;
import org.screamingsandals.lib.impl.proxy.ProxiedPlayerMapper;
import org.screamingsandals.lib.proxy.ProxiedPlayer;
import org.screamingsandals.lib.proxy.Server;
import org.screamingsandals.lib.impl.spectator.Spectator;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.annotations.internal.PlatformPluginObject;
import org.screamingsandals.lib.impl.velocity.proxy.event.PlayerLeaveEventFactory;
import org.screamingsandals.lib.impl.velocity.proxy.event.PlayerLoginEventFactory;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@ServiceDependencies(dependsOn = EventManager.class)
public class VelocityProxiedPlayerMapper extends ProxiedPlayerMapper {
    private final @NotNull ProxyServer proxyServer;

    public VelocityProxiedPlayerMapper(@PlatformPluginObject @NotNull Object plugin, @NotNull ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
        var backend = new AdventureBackend();
        new ChatEventHandlerFactory(plugin, proxyServer);
        new PlayerLoginEventFactory(plugin, proxyServer);
        new PlayerLeaveEventFactory(plugin, proxyServer);
        Spectator.setBackend(backend);
    }

    @Override
    public @Nullable Server getServer0(@NotNull String name) {
        return proxyServer.getServer(name).map(VelocityServer::new).orElse(null);
    }

    @Override
    public @NotNull List<@NotNull Server> getServers0() {
        return proxyServer.getAllServers().stream().map(VelocityServer::new).collect(Collectors.toList());
    }

    @Override
    public @Nullable ProxiedPlayer getPlayer0(@NotNull String name) {
        return proxyServer.getPlayer(name).map(VelocityProxiedPlayer::new).orElse(null);
    }

    @Override
    public @Nullable ProxiedPlayer getPlayer0(@NotNull UUID uuid) {
        return proxyServer.getPlayer(uuid).map(VelocityProxiedPlayer::new).orElse(null);
    }

    @Override
    public @NotNull List<@NotNull ProxiedPlayer> getPlayers0() {
        return proxyServer.getAllPlayers().stream().map(VelocityProxiedPlayer::new).collect(Collectors.toList());
    }

    @Override
    public @NotNull List<@NotNull ProxiedPlayer> getPlayers0(@NotNull Server serverWrapper) {
        return serverWrapper.as(RegisteredServer.class).getPlayersConnected().stream().map(VelocityProxiedPlayer::new).collect(Collectors.toList());
    }
}
