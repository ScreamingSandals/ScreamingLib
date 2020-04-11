package org.screamingsandals.lib.commands.bukkit;

import lombok.Data;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.commands.bukkit.command.BukkitCommandBase;
import org.screamingsandals.lib.commands.bukkit.command.BukkitCommandWrapper;
import org.screamingsandals.lib.commands.common.commands.SubCommand;
import org.screamingsandals.lib.commands.common.manager.CommandManager;
import org.screamingsandals.lib.commands.common.wrapper.CommandWrapper;
import org.screamingsandals.lib.debug.Debug;

import java.util.HashMap;
import java.util.Map;

@Data
public class BukkitManager implements CommandManager {
    private final Plugin plugin;
    private final CommandMapWrapper commandMapWrapper;
    private Map<String, BukkitCommandWrapper> commands = new HashMap<>();
    private HashMap<BukkitCommandWrapper, SubCommand> subCommands = new HashMap<>();

    public BukkitManager(Plugin plugin) {
        this.plugin = plugin;
        commandMapWrapper = new CommandMapWrapper(plugin);
    }

    @Override
    public void registerCommand(CommandWrapper<?, ?> commandWrapper) {
        final BukkitCommandWrapper bukkitCommandWrapper = (BukkitCommandWrapper) commandWrapper;
        final BukkitCommandBase bukkitCommandBase = bukkitCommandWrapper.getCommandBase();
        final String commandName = bukkitCommandBase.getName();

        Debug.info("Got job! Registering: " + commandName, true);

        if (!commands.containsKey(commandName) && !isCommandRegistered(commandName)) {
            commandMapWrapper.registerCommand(bukkitCommandWrapper.getCommandInstance());
        }
    }

    @Override
    public boolean isCommandRegistered(String commandName) {
        return commandMapWrapper.isCommandRegistered(commandName);
    }

    @Override
    public void unregisterCommand(String commandName) {
        commandMapWrapper.unregisterCommand(commandName);

        commands.remove(commandName);
    }
}
