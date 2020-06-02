package org.screamingsandals.lib.commands.bungee;

import lombok.Data;
import net.md_5.bungee.api.plugin.Plugin;
import org.screamingsandals.lib.commands.bungee.command.BungeeCommandWrapper;
import org.screamingsandals.lib.commands.common.manager.CommandManager;
import org.screamingsandals.lib.commands.common.wrapper.CommandWrapper;
import org.screamingsandals.lib.debug.Debug;

import java.util.HashMap;
import java.util.Map;

@Data
public class BungeeManager implements CommandManager {
    private final Plugin plugin;
    private final BungeeCommandMap bungeeCommandMap;
    private final Map<String, BungeeCommandWrapper> commands = new HashMap<>();

    public BungeeManager(Plugin plugin) {
        this.plugin = plugin;
        this.bungeeCommandMap = new BungeeCommandMap(plugin);
    }

    @Override
    public void destroy() {
        commands.values().forEach(command -> bungeeCommandMap.unregisterCommand(command.getCommandInstance()));
        commands.clear();
    }

    @Override
    public void registerCommand(CommandWrapper<?, ?> commandWrapper) {
        final var bungeeCommandWrapper = (BungeeCommandWrapper) commandWrapper;
        final var bungeeCommandBase = bungeeCommandWrapper.getCommandBase();
        final var commandName = bungeeCommandBase.getName();

        if (isCommandRegistered(commandName) || commands.containsKey(commandName)) {
            Debug.info("Command " + commandName + " is already registered!", true);
            return;
        }

        bungeeCommandMap.registerCommand(bungeeCommandWrapper.getCommandInstance());
        commands.put(commandName, bungeeCommandWrapper);
    }

    @Override
    public boolean isCommandRegistered(String commandName) {
        return bungeeCommandMap.isCommandRegistered(commandName);
    }

    @Override
    public void unregisterCommand(String commandName) {
        final var command = commands.get(commandName).getCommandInstance();

        if (command != null) {
            bungeeCommandMap.unregisterCommand(command);
        }

        commands.remove(commandName);
    }

    @Override
    public CommandWrapper<?, ?> getRegisteredCommand(String commandName) {
        return commands.get(commandName);
    }
}
