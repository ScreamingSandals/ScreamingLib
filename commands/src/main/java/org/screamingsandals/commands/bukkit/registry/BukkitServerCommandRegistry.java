package org.screamingsandals.commands.bukkit.registry;

import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.commands.api.registry.ServerCommandRegistry;
import org.screamingsandals.lib.core.util.result.Result;
import org.screamingsandals.lib.core.wrapper.plugin.PluginWrapper;

import java.util.Map;

public class BukkitServerCommandRegistry implements ServerCommandRegistry<Command> {
    private final PluginWrapper pluginWrapper;

    public BukkitServerCommandRegistry(PluginWrapper pluginWrapper) {
        this.pluginWrapper = pluginWrapper;

        Plugin plugin = pluginWrapper.getPlugin();
    }

    @Override
    public Result register(Command command) {
        return null;
    }

    @Override
    public boolean isRegistered(Command command) {
        return false;
    }

    @Override
    public Result remove(Command command) {
        return null;
    }

    @Override
    public Map<String, Command> getRegisteredCommands() {
        return null;
    }
}
