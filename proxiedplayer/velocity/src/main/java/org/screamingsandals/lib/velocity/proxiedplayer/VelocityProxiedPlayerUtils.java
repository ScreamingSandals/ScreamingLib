package org.screamingsandals.lib.velocity.proxiedplayer;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.screamingsandals.lib.proxiedplayer.ProxiedPlayerUtils;
import org.screamingsandals.lib.proxiedplayer.ProxiedPlayerWrapper;
import org.screamingsandals.lib.proxiedplayer.ServerWrapper;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class VelocityProxiedPlayerUtils extends ProxiedPlayerUtils {

    private final ProxyServer proxyServer;

    public static void init(ProxyServer proxyServer) {
        ProxiedPlayerUtils.init(() -> new VelocityProxiedPlayerUtils(proxyServer));
    }

    public VelocityProxiedPlayerUtils(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;

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
        return proxyServer.getServer(name).map(ProxiedPlayerUtils::wrapServer);
    }

    @Override
    public List<ServerWrapper> getServers0() {
        return proxyServer.getAllServers().stream().map(ProxiedPlayerUtils::wrapServer).collect(Collectors.toList());
    }

    @Override
    public Optional<ProxiedPlayerWrapper> getPlayer0(String name) {
        return proxyServer.getPlayer(name).map(ProxiedPlayerUtils::wrapPlayer);
    }

    @Override
    public Optional<ProxiedPlayerWrapper> getPlayer0(UUID uuid) {
        return proxyServer.getPlayer(uuid).map(ProxiedPlayerUtils::wrapPlayer);
    }

    @Override
    public List<ProxiedPlayerWrapper> getPlayers0() {
        return proxyServer.getAllPlayers().stream().map(ProxiedPlayerUtils::wrapPlayer).collect(Collectors.toList());
    }

    @Override
    public List<ProxiedPlayerWrapper> getPlayers0(ServerWrapper serverWrapper) {
        return serverWrapper.as(RegisteredServer.class).getPlayersConnected().stream().map(ProxiedPlayerUtils::wrapPlayer).collect(Collectors.toList());
    }
}
