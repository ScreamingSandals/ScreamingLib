package org.screamingsandals.lib.commands.bukkit;

import lombok.Data;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.commands.api.CommandManager;
import org.screamingsandals.lib.commands.common.CommandFrame;
import org.screamingsandals.lib.debug.Debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class BukkitManager implements CommandManager {
    private final Plugin plugin;
    private final CommandMapWrapper commandMapWrapper;
    private Map<String, BukkitCommandFrame> commands = new HashMap<>();
    private Map<String, List<BukkitCommandFrame>> subCommands = new HashMap<>();

    public BukkitManager(Plugin plugin) {
        this.plugin = plugin;
        commandMapWrapper = new CommandMapWrapper(plugin);
    }

    //TODO register commands and subcommands
    public void registerCommand(CommandFrame commandFrame) {
        final BukkitCommandFrame bukkitCommandFrame = (BukkitCommandFrame) commandFrame;
        final BukkitBuilder bukkitBuilder = bukkitCommandFrame.getBukkitBuilder();
        final String commandName = bukkitBuilder.getName();
        final String subCommandName = bukkitBuilder.getSubName();

        Debug.info("Got job! Registering: " + commandName + " with subCommand " + subCommandName, true);
        if (subCommandName != null && !subCommandName.equalsIgnoreCase("")) {
            Debug.info("Trying to register SubCommand " + subCommandName, true);
            if (subCommands.containsKey(commandName)) {
                Debug.info("Main command is registered, registering sub command " + subCommandName, true);
                subCommands.get(commandName).add(bukkitCommandFrame);
                return;
            } else {
                if (commands.containsKey(commandName)) {
                    Debug.info("No subCommands found, registering " + subCommandName, true);
                    subCommands.put(commandName, new ArrayList<>() {
                        {
                            add(bukkitCommandFrame);
                        }
                    });
                } else {
                    Debug.info("Main command " + commandName + " is not registered, registering and repeating.", true);
                    commandMapWrapper.registerCommand(bukkitCommandFrame.getBukkitCommand());
                    registerSubCommand(commandName, bukkitCommandFrame);
                }
            }
        }

        Debug.info("Registered command " + commandName, true);
        commandMapWrapper.registerCommand(bukkitCommandFrame.getBukkitCommand());
    }

    private void registerSubCommand(String commandName, BukkitCommandFrame frame) {
        subCommands.get(commandName).add(frame);
    }

    public boolean isCommandRegistered(String commandName) {
        return commandMapWrapper.isCommandRegistered(commandName);
    }

    public void unregisterCommand(String commandName) {
        commandMapWrapper.unregisterCommand(commandName);

        commands.remove(commandName);
    }
}
