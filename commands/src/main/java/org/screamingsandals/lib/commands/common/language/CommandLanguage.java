package org.screamingsandals.lib.commands.common.language;


import lombok.Data;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//You can create custom command messages with this!
@Data
public abstract class CommandLanguage {
    protected Map<Key, String> languages = new HashMap<>();

    public void loadDefaults() {
        put(Key.NO_PERMISSIONS, "&cYou don't have enough permissions for this command!");
        put(Key.COMMAND_DOES_NOT_EXISTS, "&cOh my, this command does not exists..");
        put(Key.SOMETHINGS_FUCKED, "&cThis command failed. Something is wrong, huh?!");
        put(Key.NOT_FOR_CONSOLE, "&cWell well well, this is not for console...");
    }

    public String get(Key langKey) {
        return languages.get(langKey);
    }

    public void put(Key langKey, String value) {
        languages.put(langKey, value);
    }

    public enum Key {
        NO_PERMISSIONS("no-permissions"),
        COMMAND_DOES_NOT_EXISTS("command-does-not-exists"),
        SOMETHINGS_FUCKED("somethings-fucked"),
        NOT_FOR_CONSOLE("not-for-console");

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
