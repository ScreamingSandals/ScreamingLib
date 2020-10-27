package org.screamingsandals.commands.bukkit.registry;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import io.papermc.lib.PaperLib;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.commands.api.registry.ServerRegistry;
import org.screamingsandals.lib.core.reflect.SReflect;
import org.screamingsandals.lib.core.util.result.Result;
import org.screamingsandals.lib.core.wrapper.plugin.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class BukkitServerRegistry implements ServerRegistry<Command> {
    private final Logger log = LoggerFactory.getLogger(ServerRegistry.class);
    private final PluginWrapper pluginWrapper;
    private final SimpleCommandMap commandMap;

    @Inject
    public BukkitServerRegistry(PluginWrapper pluginWrapper) {
        this.pluginWrapper = pluginWrapper;
        this.commandMap = Preconditions.checkNotNull(getCommandMap(), "commandMap");
    }

    @Override
    public Result register(Command command) {
        final var name = command.getName();

        if (isRegistered(name)) {
            return Result.fail("Command is already registered!");
        }

        final var result = commandMap.register(name, command);
        if (result) {
            return Result.ok();
        }
        return Result.fail("Failed to register command {}");
    }

    @Override
    public boolean isRegistered(String name) {
        return commandMap.getCommand(name) != null;
    }

    @Override
    public void remove(String name) {
        getRegisteredCommands().remove(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Command> getRegisteredCommands() {
        if (commandMap == null) {
            log.warn("knownCommands not found!");
            return Map.of();
        }

        Map<String, Command> toReturn;
        if (PaperLib.isPaper()) {
            toReturn = commandMap.getKnownCommands();
        } else {
            log.debug("Paper not found, using reflection for getting registered commands!");
            toReturn = (Map<String, Command>) SReflect.getField(commandMap, "knownCommands");
        }
        return toReturn;
    }

    private SimpleCommandMap getCommandMap() {
        final var plugin = (Plugin) pluginWrapper.getPlugin();
        final var server = plugin.getServer();

        if (PaperLib.isPaper()) {
            log.debug("Found Paper, got the SimpleCommandMap!");
            return (SimpleCommandMap) server.getCommandMap();
        } else {
            log.debug("Paper not found, using reflection");
            return (SimpleCommandMap) SReflect.getField(server, "commandMap");
        }
    }
}
