package org.screamingsandals.commands.bungee.registry;

import com.google.inject.Inject;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import org.screamingsandals.commands.api.registry.ServerRegistry;
import org.screamingsandals.lib.core.util.result.Result;
import org.screamingsandals.lib.core.wrapper.plugin.PluginWrapper;

import java.util.Map;
import java.util.stream.Collectors;

public class BungeeServerRegistry implements ServerRegistry<Command> {
    private final Plugin plugin;
    private final PluginManager pluginManager;

    @Inject
    public BungeeServerRegistry(PluginWrapper pluginWrapper) {
        this.plugin = pluginWrapper.getPlugin();
        this.pluginManager = plugin.getProxy().getPluginManager();
    }

    @Override
    public Result register(Command command) {
        if (isRegistered(command.getName())) {
            return Result.fail("Command already registered!");
        }

        pluginManager.registerCommand(plugin, command);
        return Result.ok();
    }

    @Override
    public boolean isRegistered(String name) {
        return getRegisteredCommands().containsKey(name);
    }

    @Override
    public void remove(String name) {
        final var command = getRegisteredCommands().get(name);

        if (command == null) {
            return;
        }

        pluginManager.unregisterCommand(command);
    }

    @Override
    public Map<String, Command> getRegisteredCommands() {
        return pluginManager.getCommands()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
