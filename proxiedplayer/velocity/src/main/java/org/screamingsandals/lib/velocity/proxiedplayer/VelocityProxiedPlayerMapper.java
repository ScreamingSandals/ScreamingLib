package org.screamingsandals.lib.velocity.proxiedplayer;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.screamingsandals.lib.proxiedplayer.ProxiedPlayerMapper;
import org.screamingsandals.lib.proxiedplayer.ProxiedPlayerWrapper;
import org.screamingsandals.lib.proxiedplayer.ServerWrapper;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.PlatformMapping;
import org.screamingsandals.lib.velocity.proxiedplayer.event.ChatEventHandlerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@PlatformMapping(platform = PlatformType.VELOCITY)
public class VelocityProxiedPlayerMapper extends ProxiedPlayerMapper {

    private final ProxyServer proxyServer;

    public static void init(Object plugin, ProxyServer proxyServer) {
        ProxiedPlayerMapper.init(() -> new VelocityProxiedPlayerMapper(plugin, proxyServer));
    }

    public VelocityProxiedPlayerMapper(Object plugin, ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
        new ChatEventHandlerFactory(plugin, proxyServer);

        /* NOTE: Converter needs null, so don't blame me because you see orElse(null) */

        playerConverter
                .registerP2W(Player.class, player -> new ProxiedPlayerWrapper(player.getUsername(), player.getUniqueId()))
                .registerW2P(Player.class, proxiedPlayerWrapper -> this.proxyServer.getPlayer(proxiedPlayerWrapper.getUuid()).orElse(null));

        serverConverter
                .registerP2W(ServerInfo.class, serverInfo -> new ServerWrapper(serverInfo.getName(), serverInfo.getAddress()))
                .registerP2W(RegisteredServer.class, registeredServer -> new ServerWrapper(registeredServer.getServerInfo().getName(), registeredServer.getServerInfo().getAddress()))
                .registerW2P(ServerInfo.class, serverWrapper -> this.proxyServer.getServer(serverWrapper.getName()).map(RegisteredServer::getServerInfo).orElse(null))
                .registerW2P(RegisteredServer.class, serverWrapper -> this.proxyServer.getServer(serverWrapper.getName()).orElse(null));
    }

    @Override
    public void sendMessage0(ProxiedPlayerWrapper playerWrapper, String message) {
        playerWrapper.as(Player.class).sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
    }

    @Override
    public void switchServer0(ProxiedPlayerWrapper playerWrapper, ServerWrapper server) {
        playerWrapper.as(Player.class).createConnectionRequest(server.as(RegisteredServer.class));
    }

    @Override
    public Optional<ServerWrapper> getServer0(String name) {
        return proxyServer.getServer(name).map(ProxiedPlayerMapper::wrapServer);
    }

    @Override
    public List<ServerWrapper> getServers0() {
        return proxyServer.getAllServers().stream().map(ProxiedPlayerMapper::wrapServer).collect(Collectors.toList());
    }

    @Override
    public Optional<ProxiedPlayerWrapper> getPlayer0(String name) {
        return proxyServer.getPlayer(name).map(ProxiedPlayerMapper::wrapPlayer);
    }

    @Override
    public Optional<ProxiedPlayerWrapper> getPlayer0(UUID uuid) {
        return proxyServer.getPlayer(uuid).map(ProxiedPlayerMapper::wrapPlayer);
    }

    @Override
    public List<ProxiedPlayerWrapper> getPlayers0() {
        return proxyServer.getAllPlayers().stream().map(ProxiedPlayerMapper::wrapPlayer).collect(Collectors.toList());
    }

    @Override
    public List<ProxiedPlayerWrapper> getPlayers0(ServerWrapper serverWrapper) {
        return serverWrapper.as(RegisteredServer.class).getPlayersConnected().stream().map(ProxiedPlayerMapper::wrapPlayer).collect(Collectors.toList());
    }
}
