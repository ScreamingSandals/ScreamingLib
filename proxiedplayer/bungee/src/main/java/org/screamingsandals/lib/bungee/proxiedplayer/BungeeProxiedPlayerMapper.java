package org.screamingsandals.lib.bungee.proxiedplayer;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Plugin;
import org.screamingsandals.lib.bungee.proxiedplayer.listener.ChatEventListener;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.proxiedplayer.ProxiedPlayerMapper;
import org.screamingsandals.lib.proxiedplayer.ProxiedPlayerWrapper;
import org.screamingsandals.lib.proxiedplayer.ProxiedSenderWrapper;
import org.screamingsandals.lib.proxiedplayer.ServerWrapper;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.permissions.*;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.List;
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
}
