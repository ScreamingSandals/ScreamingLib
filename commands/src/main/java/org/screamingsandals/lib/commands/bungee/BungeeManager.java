package org.screamingsandals.lib.commands.bungee;

import lombok.Data;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import org.screamingsandals.lib.commands.api.CommandManager;
import org.screamingsandals.lib.commands.common.CommandFrame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class BungeeManager implements CommandManager {
    private final Plugin plugin;
    private final PluginManager pluginManager;
    private Map<String, BungeeCommandFrame> commands = new HashMap<>();
    private Map<String, List<BungeeCommandFrame>> subCommands = new HashMap<>();

    public BungeeManager(Plugin plugin) {
        this.plugin = plugin;
        this.pluginManager = plugin.getProxy().getPluginManager();
    }

    @Override
    public void registerCommand(CommandFrame commandFrame) {
        final BungeeCommandFrame bungeeCommandFrame = (BungeeCommandFrame) commandFrame;

        pluginManager.registerCommand(plugin, bungeeCommandFrame.getBungeeCommandWrapper());
        commands.put(bungeeCommandFrame.getBungeeBuilder().getName(), bungeeCommandFrame);
    }

    @Override
    public boolean isCommandRegistered(String commandName) {
        for (Map.Entry<String, Command> registeredCommand : pluginManager.getCommands()) {
            if (registeredCommand.getKey().equals(commandName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void unregisterCommand(String commandName) {
        pluginManager.unregisterCommand(commands.get(commandName).getBungeeCommandWrapper());
    }
}
