package org.screamingsandals.lib.config;

import lombok.Data;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.Map;

@Data
public abstract class SpigotConfigAdapter implements ConfigAdapter {
    private final File configFile;
    private YamlConfiguration yamlConfiguration;

    public static SpigotConfigAdapter create(File configFile) {
        return new SpigotConfigAdapter(configFile) {
        };
    }

    @Override
    public void load() {
        yamlConfiguration = new YamlConfiguration();

        try {
            yamlConfiguration.load(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        try {
            yamlConfiguration.save(configFile);
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object get(String key) {
        return yamlConfiguration.get(key);
    }

    @Override
    public String getString(String key) {
        return yamlConfiguration.getString(key);
    }

    @Override
    public String getString(String key, String def) {
        return yamlConfiguration.getString(key, def);
    }

    @Override
    public boolean getBoolean(String key) {
        return yamlConfiguration.getBoolean(key);
    }

    @Override
    public boolean getBoolean(String key, boolean def) {
        return yamlConfiguration.getBoolean(key, def);
    }

    @Override
    public int getInt(String key) {
        return yamlConfiguration.getInt(key);
    }

    @Override
    public int getInt(String key, int def) {
        return yamlConfiguration.getInt(key, def);
    }

    @Override
    public double getDouble(String key) {
        return yamlConfiguration.getDouble(key);
    }

    @Override
    public double getDouble(String key, double def) {
        return yamlConfiguration.getDouble(key, def);
    }

    @Override
    public List<?> getList(String key) {
        return yamlConfiguration.getList(key);
    }

    @Override
    public List<String> getStringList(String key) {
        return yamlConfiguration.getStringList(key);
    }

    @Override
    public List<Map<?, ?>> getMap(String key) {
        return yamlConfiguration.getMapList(key);
    }

    @Override
    public boolean isSet(String key) {
        return yamlConfiguration.isSet(key);
    }

    @Override
    public void set(String key, Object obj) {
        yamlConfiguration.set(key, obj);
    }
}
