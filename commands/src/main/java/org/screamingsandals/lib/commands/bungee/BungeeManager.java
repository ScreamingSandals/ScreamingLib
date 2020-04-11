package org.screamingsandals.lib.commands.bungee;

import lombok.Data;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import org.screamingsandals.lib.commands.bungee.command.BungeeCommandBase;
import org.screamingsandals.lib.commands.bungee.command.BungeeCommandWrapper;
import org.screamingsandals.lib.commands.common.commands.SubCommand;
import org.screamingsandals.lib.commands.common.manager.CommandManager;
import org.screamingsandals.lib.commands.common.wrapper.CommandWrapper;
import org.screamingsandals.lib.debug.Debug;

import java.util.HashMap;
import java.util.Map;

@Data
public class BungeeManager implements CommandManager {
    private final Plugin plugin;
    private final BungeeCommandMap bungeeCommandMap;
    private Map<String, BungeeCommandWrapper> commands = new HashMap<>();
    private HashMap<BungeeCommandWrapper, SubCommand> subCommands = new HashMap<>();

    public BungeeManager(Plugin plugin) {
        this.plugin = plugin;
        this.bungeeCommandMap = new BungeeCommandMap(plugin);
    }

    @Override
    public void registerCommand(CommandWrapper<?, ?> commandWrapper) {
        final BungeeCommandWrapper bungeeCommandWrapper = (BungeeCommandWrapper) commandWrapper;
        final BungeeCommandBase bungeeCommandBase = bungeeCommandWrapper.getCommandBase();
        final String commandName = bungeeCommandBase.getName();

        Debug.info("Got job! Registering: " + commandName, true);

        if (!commands.containsKey(commandName) && !isCommandRegistered(commandName)) {
            bungeeCommandMap.registerCommand(bungeeCommandWrapper.getCommandInstance());
            commands.put(commandName, bungeeCommandWrapper);
        }
    }

    @Override
    public boolean isCommandRegistered(String commandName) {
        return bungeeCommandMap.isCommandRegistered(commandName);
    }

    @Override
    public void unregisterCommand(String commandName) {
        final Command command = commands.get(commandName).getCommandInstance();

        if (command != null) {
            bungeeCommandMap.unregisterCommand(command);
        }

        commands.remove(commandName);
    }
}
