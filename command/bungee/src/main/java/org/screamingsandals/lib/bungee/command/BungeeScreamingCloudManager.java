package org.screamingsandals.lib.bungee.command;

import cloud.commandframework.CommandTree;
import cloud.commandframework.bungee.BungeeCommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.screamingsandals.lib.proxy.ProxiedPlayerMapper;
import org.screamingsandals.lib.proxy.ProxiedSenderWrapper;
import org.screamingsandals.lib.sender.CommandSenderWrapper;

import java.util.function.Function;

public class BungeeScreamingCloudManager extends BungeeCommandManager<CommandSenderWrapper> {
    /**
     * Construct a new Bungee command manager
     *
     * @param owningPlugin       Plugin that is constructing the manager
     * @param commandCoordinator Coordinator provider
     */
    public BungeeScreamingCloudManager(Plugin owningPlugin,
                                       Function<CommandTree<CommandSenderWrapper>, CommandExecutionCoordinator<CommandSenderWrapper>> commandCoordinator) {
        super(owningPlugin, commandCoordinator, sender -> {
            if (sender instanceof ProxiedPlayer) {
                return ProxiedPlayerMapper.wrapPlayer(sender);
            }
            return ProxiedPlayerMapper.wrapSender(sender);
        }, sender -> {
            if (sender.getType() == ProxiedSenderWrapper.Type.PLAYER) {
                return sender.as(ProxiedPlayer.class);
            }
            return sender.as(CommandSender.class);
        });
    }
}
