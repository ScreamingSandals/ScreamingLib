package org.screamingsandals.lib.lang.files.config;

import org.screamingsandals.lib.lang.Language;

import java.io.File;
import java.io.Reader;
import java.util.List;
import java.util.Map;

/**
 * @author ScreamingSandals team
 */
public interface ConfigAdapter {
    static ConfigAdapter create(Reader reader) {
        if (Language.isSpigot()) {
            return new BukkitConfig(reader);
        } else {
            return new BungeeConfig(reader);
        }
    }

    static ConfigAdapter create(File file) {
        if (Language.isSpigot()) {
            return new BukkitConfig(file);
        } else {
            return new BungeeConfig(file);
        }
    }

    Object get(String key);

    String getString(String key);

    String getString(String key, String def);

    boolean getBoolean(String key);

    boolean getBoolean(String key, boolean def);

    int getInt(String key);

    int getInt(String key, int def);

    double getDouble(String key);

    double getDouble(String key, double def);

    List<?> getList(String key);

    List<Map<?, ?>> getMap(String key);

    List<String> getStringList(String key);

    boolean isSet(String key);

    void set(String key, Object obj);
}
