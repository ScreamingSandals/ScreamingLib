package org.screamingsandals.lib.gamecore.language;

import org.screamingsandals.lib.lang.Base;

import java.io.File;

public class GameLanguage extends Base {

    public GameLanguage(Object plugin) {
        super(plugin);
    }

    public GameLanguage(Object plugin, String globalLanguage) {
        super(plugin, globalLanguage);
    }

    public GameLanguage(Object plugin, String globalLanguage, String customPrefix) {
        super(plugin, globalLanguage, customPrefix);
    }

    public GameLanguage(Object plugin, File customDataFolder) {
        super(plugin, customDataFolder);
    }

    public GameLanguage(Object plugin, String globalLanguage, File customDataFolder, String customPrefix) {
        super(plugin, globalLanguage, customDataFolder, customPrefix);
    }

    public static GameMessage mpr() {
        return m(null, null, true);
    }

    public static GameMessage mpr(String key) {
        return m(key, null, true);
    }

    public static GameMessage mpr(String key, String def) {
        return m(key, def, true);
    }


    public static GameMessage m() {
        return m(null, null, false);
    }

    public static GameMessage m(String key) {
        return m(key, null, false);
    }

    public static GameMessage m(String key, boolean prefix) {
        return m(key, null, prefix);
    }

    public static GameMessage m(String key, String def) {
        return m(key, def, false);
    }

    public static GameMessage m(String key, String def, boolean prefix) {
        return new GameMessage(key, Base.getGlobalStorage(), def, prefix);
    }
}
