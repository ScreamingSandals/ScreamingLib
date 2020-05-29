package org.screamingsandals.lib.lang;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.config.BungeeConfigAdapter;
import org.screamingsandals.lib.config.ConfigAdapter;
import org.screamingsandals.lib.config.SpigotConfigAdapter;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ScreamingSandals team
 */
public class Utils {
    public static String colorize(String text) {
        try {
            Class.forName("net.md_5.bungee.api.ProxyServer");
            return ChatColor.translateAlternateColorCodes('&', text);
        } catch (ClassNotFoundException ignored) {
            return org.bukkit.ChatColor.translateAlternateColorCodes('&', text);
        }
    }

    public static List<String> colorize(List<String> text) {
        List<String> toReturn = new ArrayList<>();
        for (String string : text) {
            if (string.equals(" ")){
                string = "&r ";
            }

            try {
                Class.forName("net.md_5.bungee.api.ProxyServer");
                toReturn.add(ChatColor.translateAlternateColorCodes('&', string));
            } catch (ClassNotFoundException ignored) {
                toReturn.add(org.bukkit.ChatColor.translateAlternateColorCodes('&', string));
            }
        }
        return toReturn;
    }

    public static void sendPluginMessage(String message) {
        Object providedPlugin = Base.getInstance().getPlugin();
        String pluginName;
        if (Base.isSpigot()) {
            Plugin plugin = (Plugin) providedPlugin;
            pluginName = plugin.getName();

            plugin.getLogger().info(message.replace("%pluginName%", pluginName));
        } else {
            net.md_5.bungee.api.plugin.Plugin plugin = (net.md_5.bungee.api.plugin.Plugin) providedPlugin;
            pluginName = plugin.getDescription().getName();

            plugin.getLogger().info(message.replace("%pluginName%", pluginName));
        }
    }

    public static ConfigAdapter createConfig(Object input) {
        if (input instanceof InputStream) {
            final var stream = (InputStream) input;
            if (Base.isSpigot()) {
                return SpigotConfigAdapter.create(stream);
            } else {
                return BungeeConfigAdapter.create(stream);
            }
        } else if (input instanceof File) {
            final var stream = (File) input;
            if (Base.isSpigot()) {
                return SpigotConfigAdapter.create(stream);
            } else {
                return BungeeConfigAdapter.create(stream);
            }
        }
        return null;
    }

    public static File getDataFolder(Object plugin) {
        try {
            return (File) plugin.getClass().getMethod("getDataFolder").invoke(plugin);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
