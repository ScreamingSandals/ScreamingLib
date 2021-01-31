package org.screamingsandals.lib.velocity.command;

import cloud.commandframework.CommandTree;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.velocity.VelocityCommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.screamingsandals.lib.proxiedplayer.ProxiedPlayerMapper;
import org.screamingsandals.lib.proxiedplayer.ProxiedSenderWrapper;

import java.util.function.Function;

public class VelocityScreamingCloudManager extends VelocityCommandManager<ProxiedSenderWrapper> {

    /**
     * Construct a new Velocity command manager
     *
     * @param plugin             Container for the owning plugin. Nullable for backwards compatibility
     * @param proxyServer        ProxyServer instance
     * @param commandCoordinator Coordinator provider
     */
    public VelocityScreamingCloudManager(PluginContainer plugin,
                                         ProxyServer proxyServer,
                                         Function<CommandTree<ProxiedSenderWrapper>, CommandExecutionCoordinator<ProxiedSenderWrapper>> commandCoordinator) {
        super(plugin, proxyServer, commandCoordinator, sender -> {
            if (sender instanceof Player) {
                return ProxiedPlayerMapper.wrapPlayer(sender);
            }
            return ProxiedPlayerMapper.wrapSender(sender);
        }, sender -> {
            if (sender.getType() == ProxiedSenderWrapper.Type.PLAYER) {
                return sender.as(Player.class);
            }
            return sender.as(CommandSource.class);
        });
    }
}
