package org.screamingsandals.lib.velocity.proxiedplayer;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.proxiedplayer.ProxiedPlayerMapper;
import org.screamingsandals.lib.proxiedplayer.ProxiedPlayerWrapper;
import org.screamingsandals.lib.proxiedplayer.ProxiedSenderWrapper;
import org.screamingsandals.lib.proxiedplayer.ServerWrapper;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.permissions.*;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.velocity.proxiedplayer.event.ChatEventHandlerFactory;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service(dependsOn = {
        EventManager.class
})
public class VelocityProxiedPlayerMapper extends ProxiedPlayerMapper {
    private final static String CONSOLE_NAME = "CONSOLE";
    private final ProxyServer proxyServer;

    public static void init(Object plugin, ProxyServer proxyServer) {
        ProxiedPlayerMapper.init(() -> new VelocityProxiedPlayerMapper(plugin, proxyServer));
    }

    public VelocityProxiedPlayerMapper(Object plugin, ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
        new ChatEventHandlerFactory(plugin, proxyServer);

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
    public void sendMessage0(ProxiedSenderWrapper wrapper, String message) {
        wrapper.as(CommandSource.class).sendMessage(AdventureHelper.toComponent(message));
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

    @Override
    public boolean hasPermission0(CommandSenderWrapper wrapper, Permission permission) {
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
    public boolean isPermissionSet0(CommandSenderWrapper wrapper, Permission permission) {
        if (permission instanceof SimplePermission) {
            return wrapper.as(CommandSource.class).getPermissionValue(((SimplePermission) permission).getPermissionString()) == Tristate.UNDEFINED;
        }
        return true;
    }

    @Override
    public Locale getLocale0(ProxiedSenderWrapper wrapper) {
        return wrapper.asOptional(Player.class)
                .map(player -> player.getPlayerSettings().getLocale())
                .orElse(Locale.US);
    }
}
