package org.screamingsandals.lib.commands.api.core;

import lombok.Data;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.commands.api.CommandManager;
import org.screamingsandals.lib.commands.bukkit.BukkitManager;
import org.screamingsandals.lib.commands.bungee.BungeeManager;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.lang.Language;

@Data
public abstract class CommandsBase {
    private final Object plugin;
    private static CommandsBase instance;
    private CommandManager commandManager;
    private Language language;

    public CommandsBase(Object plugin, Language language) {
        this.plugin = plugin;
        this.language = language;
        instance = this;
    }

    public CommandsBase(Object plugin) {
       this(plugin, null);
    }

    public void load() {
        try {
            Class.forName("org.bukkit.Server");
            commandManager = new BukkitManager((Plugin) plugin);
        } catch (Throwable ignored) {
            try {
                commandManager = new BungeeManager((net.md_5.bungee.api.plugin.Plugin) plugin);
                Class.forName("net.md_5.bungee.api.plugin.PluginManager");
            } catch (Throwable ignored2) {
                Debug.warn("Your server type is not supported!");
            }
        }
    }

    public static CommandsBase getInstance() {
        return instance;
    }
}
