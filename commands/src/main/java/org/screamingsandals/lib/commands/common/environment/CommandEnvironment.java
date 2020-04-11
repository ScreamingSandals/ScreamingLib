package org.screamingsandals.lib.commands.common.environment;

import lombok.Data;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.commands.common.manager.CommandManager;
import org.screamingsandals.lib.commands.bukkit.BukkitManager;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.lang.Language;

@Data
public abstract class CommandEnvironment {
    private final Object plugin;
    private Language language;
    private static CommandEnvironment instance;
    private CommandManager commandManager;

    public CommandEnvironment(Object plugin, Language language) {
        this.plugin = plugin;
        this.language = language;
        instance = this;
    }

    public CommandEnvironment(Object plugin) {
       this(plugin, null);
    }

    public void load() {
        try {
            Class.forName("org.bukkit.Server");
            commandManager = new BukkitManager((Plugin) plugin);
        } catch (Throwable ignored) {
            try {
                //commandManager = new BungeeManager((net.md_5.bungee.api.plugin.Plugin) plugin); //TODO
                Class.forName("net.md_5.bungee.api.plugin.PluginManager");
            } catch (Throwable ignored2) {
                Debug.warn("Your server type is not supported!");
            }
        }
    }

    public static CommandEnvironment getInstance() {
        return instance;
    }

    public static void sendPluginMessage() {

    }
}
