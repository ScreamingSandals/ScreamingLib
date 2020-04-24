package org.screamingsandals.lib.commands.common.language;


import lombok.Data;
import net.md_5.bungee.api.ChatColor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//You can create custom command messages with this!
@Data
public abstract class CommandsLanguage {
    private Map<LangKey, String> languages = new HashMap<>();

    public void loadDefaults() {
        languages.put(LangKey.NO_PERMISSIONS, "&cYou don't have enough permissions for this command!");
        languages.put(LangKey.COMMAND_DOES_NOT_EXISTS, "&cOh my, this command does not exists..");
        languages.put(LangKey.SOMETHINGS_FUCKED, "&cThis command failed. Something is wrong, huh?!");
        languages.put(LangKey.NOT_FOR_CONSOLE, "&cWell well well, this is not for console...");
    }

    public String get(LangKey langKey) {
        return languages.get(langKey);
    }

    public void add(LangKey langKey, String string) {
        languages.put(langKey, string);
    }

    public enum LangKey {
        NO_PERMISSIONS,
        COMMAND_DOES_NOT_EXISTS,
        SOMETHINGS_FUCKED,
        NOT_FOR_CONSOLE;
    }

    public void sendMessage(Object receiver, LangKey langKey) {
        if (receiver instanceof Collection) {
            for (Object rec : (Collection<?>) receiver) {
                sendMessage(rec, langKey);
            }
        }
        try {
            receiver.getClass().getMethod("sendMessage", String.class).invoke(receiver, colorize(get(langKey)));
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
