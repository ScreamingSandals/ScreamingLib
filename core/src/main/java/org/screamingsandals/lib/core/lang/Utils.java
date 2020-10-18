package org.screamingsandals.lib.core.lang;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.reflection.Reflection;

import java.io.File;
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
        Object providedPlugin = LanguageBase.getInstance().getPlugin();
        String pluginName;
        if (LanguageBase.isSpigot()) {
            Plugin plugin = (Plugin) providedPlugin;
            pluginName = plugin.getName();

            plugin.getLogger().info(message.replace("%pluginName%", pluginName));
        } else {
            net.md_5.bungee.api.plugin.Plugin plugin = (net.md_5.bungee.api.plugin.Plugin) providedPlugin;
            pluginName = plugin.getDescription().getName();

            plugin.getLogger().info(message.replace("%pluginName%", pluginName));
        }
    }

    public static File getDataFolder(Object plugin) {
        try {
            return (File) Reflection.fastInvoke(plugin, "getDataFolder");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
