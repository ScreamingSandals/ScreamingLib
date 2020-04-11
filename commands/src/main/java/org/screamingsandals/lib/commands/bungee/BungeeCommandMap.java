package org.screamingsandals.lib.commands.bungee;

import lombok.Data;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.Map;

@Data
public class BungeeCommandMap {
    private final Plugin plugin;
    private final PluginManager pluginManager;

    public BungeeCommandMap(Plugin plugin) {
        this.plugin = plugin;
        pluginManager = this.plugin.getProxy().getPluginManager();
    }

    public void registerCommand(Command command) {
        pluginManager.registerCommand(plugin, command);
    }

    public boolean isCommandRegistered(String commandName) {
        for (Map.Entry<String, Command> registeredCommand : pluginManager.getCommands()) {
            if (registeredCommand.getKey().equals(commandName)) {
                return true;
            }
        }
        return false;
    }

    public void unregisterCommand(Command command) {
        pluginManager.unregisterCommand(command);
    }
}
