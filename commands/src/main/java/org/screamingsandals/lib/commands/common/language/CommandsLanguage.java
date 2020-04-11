package org.screamingsandals.lib.commands.common.language;


import lombok.Data;
import net.md_5.bungee.api.ChatColor;

import java.util.Collection;

//You can create custom command messages with this!
@Data
public abstract class CommandsLanguage {
    private String noPermissions = "&cYou don't have enough permissions for this command!";
    private String somethingsFucked = "&cThis command failed. Something is wrong, huh?!";

    public void sendMessage(Object receiver, String text) {
        if (receiver instanceof Collection) {
            for (Object rec : (Collection<?>) receiver) {
                sendMessage(rec, text);
            }
        }
        try {
            receiver.getClass().getMethod("sendMessage", String.class).invoke(receiver, colorize(text));
        } catch (Throwable ignored) {
        }
    }

    /**
     * Let's colorize the text that is sent.
     * @param text to be colorized
     * @return colorized text
     */
    public static String colorize(String text) {
        try {
            Class.forName("net.md_5.bungee.api.ChatColor");
            return ChatColor.translateAlternateColorCodes('&', text);
        } catch (ClassNotFoundException ignored) {
            return org.bukkit.ChatColor.translateAlternateColorCodes('&', text);
        }
    }
}
