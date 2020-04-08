package org.screamingsandals.lib.commands.bukkit;

import lombok.Data;
import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.commands.api.CommandManager;
import org.screamingsandals.lib.commands.common.CommandFrame;

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

        if (subCommandName != null) {
            if (subCommands.containsKey(commandName)) {

            }
        }

        commandMapWrapper.registerCommand(bukkitCommandFrame.getBukkitCommand());
    }

    public void registerCommand(Command command) {
        commandMapWrapper.registerCommand(command);
    }


    public boolean isCommandRegistered(String commandName) {
        return commandMapWrapper.isCommandRegistered(commandName);
    }

    public void unregisterCommand(String commandName) {
        commandMapWrapper.removeRegisteredCommand(commandName);

        commands.remove(commandName);
    }
}
