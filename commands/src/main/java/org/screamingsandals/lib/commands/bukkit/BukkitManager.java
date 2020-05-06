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
    private final BukkitCommandMap bukkitCommandMap;
    private Map<String, BukkitCommandWrapper> commands = new HashMap<>();
    private HashMap<BukkitCommandWrapper, SubCommand> subCommands = new HashMap<>();

    public BukkitManager(Plugin plugin) {
        this.plugin = plugin;
        this.bukkitCommandMap = new BukkitCommandMap(plugin);
    }

    @Override
    public void destroy() {
        commands.keySet().forEach(bukkitCommandMap::unregisterCommand);
        commands.clear();

        subCommands.clear();
    }

    @Override
    public void registerCommand(CommandWrapper<?, ?> commandWrapper) {
        final BukkitCommandWrapper bukkitCommandWrapper = (BukkitCommandWrapper) commandWrapper;
        final BukkitCommandBase bukkitCommandBase = bukkitCommandWrapper.getCommandBase();
        final String commandName = bukkitCommandBase.getName();

        Debug.info("Got job! Registering: " + commandName, true);

        if (!commands.containsKey(commandName) && !isCommandRegistered(commandName)) {
            bukkitCommandMap.registerCommand(bukkitCommandWrapper.getCommandInstance());
            commands.put(commandName, bukkitCommandWrapper);
        } else {
            Debug.info("Command " + commandName + " is already registered!", true);
        }
    }

    @Override
    public boolean isCommandRegistered(String commandName) {
        return bukkitCommandMap.isCommandRegistered(commandName);
    }

    @Override
    public void unregisterCommand(String commandName) {
        bukkitCommandMap.unregisterCommand(commandName);

        commands.remove(commandName);
    }

    @Override
    public CommandWrapper<?, ?> getRegisteredCommand(String commandName) {
        System.out.println(commands.get(commandName));
        return commands.get(commandName);
    }

    @Override
    public void registerSubCommand(CommandWrapper<?, ?> commandWrapper, SubCommand subCommand) {
        subCommands.put((BukkitCommandWrapper) commandWrapper, subCommand);
    }
}
