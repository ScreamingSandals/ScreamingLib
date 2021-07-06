package org.screamingsandals.lib.bungee.command;

import cloud.commandframework.CommandManager;
import cloud.commandframework.CommandTree;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import net.md_5.bungee.api.plugin.Plugin;
import org.screamingsandals.lib.bungee.proxiedplayer.BungeeProxiedPlayerMapper;
import org.screamingsandals.lib.command.CloudConstructor;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.function.Function;

@Service(dependsOn = {
        BungeeProxiedPlayerMapper.class
})
public class BungeeCloudConstructor extends CloudConstructor {
    private final Plugin plugin;

    public static void init(Plugin plugin) {
        CloudConstructor.init(() -> new BungeeCloudConstructor(plugin));
    }

    public BungeeCloudConstructor(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandManager<CommandSenderWrapper> construct0(Function<CommandTree<CommandSenderWrapper>, CommandExecutionCoordinator<CommandSenderWrapper>> commandCoordinator) {
        return new BungeeScreamingCloudManager(plugin, commandCoordinator);
    }
}
