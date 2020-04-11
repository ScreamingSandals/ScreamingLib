package org.screamingsandals.lib.commands.common.environment;

import lombok.Data;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.commands.bukkit.BukkitManager;
import org.screamingsandals.lib.commands.common.language.CommandsLanguage;
import org.screamingsandals.lib.commands.common.language.DefaultLanguage;
import org.screamingsandals.lib.commands.common.manager.CommandManager;
import org.screamingsandals.lib.debug.Debug;

@Data
public abstract class CommandEnvironment {
    private final Object plugin;
    private static CommandEnvironment instance;
    private CommandsLanguage commandLanguage;
    private CommandManager commandManager;

    public CommandEnvironment(Object plugin) {
        this.plugin = plugin;
        instance = this;
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

        commandLanguage = new DefaultLanguage();
    }

    public static CommandEnvironment getInstance() {
        return instance;
    }

    public static void sendPluginMessage() {

    }
}
