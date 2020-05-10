package org.screamingsandals.lib.commands.common.language;


import lombok.Data;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//You can create custom command messages with this!
@Data
public abstract class CommandsLanguage {
    private Map<Key, String> languages = new HashMap<>();

    public void loadDefaults() {
        languages.put(Key.NO_PERMISSIONS, "&cYou don't have enough permissions for this command!");
        languages.put(Key.COMMAND_DOES_NOT_EXISTS, "&cOh my, this command does not exists..");
        languages.put(Key.SOMETHINGS_FUCKED, "&cThis command failed. Something is wrong, huh?!");
        languages.put(Key.NOT_FOR_CONSOLE, "&cWell well well, this is not for console...");
    }

    public String get(Key langKey) {
        return languages.get(langKey);
    }

    public void add(Key langKey, String value) {
        languages.put(langKey, value);
    }

    public void replace(Key langKey, String value) {
        languages.remove(langKey);
        languages.put(langKey, value);
    }

    public enum Key {
        NO_PERMISSIONS("no_permissions"),
        COMMAND_DOES_NOT_EXISTS("command_does_not_exists"),
        SOMETHINGS_FUCKED("somethings_fucked"),
        NOT_FOR_CONSOLE("not_for_console");

        @Getter
        private final String langKey;

        Key(String langKey) {
            this.langKey = langKey;
        }
    }

    public void sendMessage(Object receiver, Key langKey) {
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
