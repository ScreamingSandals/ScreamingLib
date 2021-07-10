package org.screamingsandals.lib.bukkit.command;

import cloud.commandframework.CommandManager;
import cloud.commandframework.CommandTree;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.player.BukkitPlayerMapper;
import org.screamingsandals.lib.command.CloudConstructor;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.function.Function;

@Service(dependsOn = {
        BukkitPlayerMapper.class
})
public class BukkitCloudConstructor extends CloudConstructor {
    private final Plugin plugin;

    public static void init(Plugin plugin, Controllable controllable) {
        CloudConstructor.init(() -> new BukkitCloudConstructor(plugin));
    }

    public BukkitCloudConstructor(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandManager<CommandSenderWrapper> construct0(Function<CommandTree<CommandSenderWrapper>, CommandExecutionCoordinator<CommandSenderWrapper>> commandCoordinator) throws Exception {
        return new PaperScreamingCloudManager(plugin, commandCoordinator);
    }
}
