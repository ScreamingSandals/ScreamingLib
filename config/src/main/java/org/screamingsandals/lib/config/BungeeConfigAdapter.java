package org.screamingsandals.lib.config;

import lombok.Data;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public abstract class BungeeConfigAdapter implements ConfigAdapter {
    private final File configFile;
    private Configuration configuration;

    @Override
    public void load() {
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, configFile);
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object get(String key) {
        return configuration.get(key);
    }

    @Override
    public String getString(String key) {
        return configuration.getString(key);
    }

    @Override
    public String getString(String key, String def) {
        return configuration.getString(key, def);
    }

    @Override
    public boolean getBoolean(String key) {
        return configuration.getBoolean(key);
    }

    @Override
    public boolean getBoolean(String key, boolean def) {
        return configuration.getBoolean(key, def);
    }

    @Override
    public int getInt(String key) {
        return configuration.getInt(key);
    }

    @Override
    public int getInt(String key, int def) {
        return configuration.getInt(key, def);
    }

    @Override
    public double getDouble(String key) {
        return configuration.getDouble(key);
    }

    @Override
    public double getDouble(String key, double def) {
        return configuration.getDouble(key, def);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> getList(String key) {
        return (List<Object>) configuration.getList(key);
    }

    @Override
    public List<String> getStringList(String key) {
        return configuration.getStringList(key);
    }

    @Override
    public List<Map<?, ?>> getMap(String key) {
        List<?> list = getList(key);
        List<Map<?, ?>> result = new ArrayList<>();

        if (list == null) {
            return result;
        }

        for (Object object : list) {
            if (object instanceof Map) {
                result.add((Map<?, ?>) object);
            }
        }

        return result;
    }

    @Override
    public boolean isSet(String key) {
        return configuration.contains(key);
    }

    @Override
    public void set(String key, Object obj) {
        configuration.set(key, obj);
    }
}
