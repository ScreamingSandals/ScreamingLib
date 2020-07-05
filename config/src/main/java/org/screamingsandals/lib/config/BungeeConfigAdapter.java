package org.screamingsandals.lib.config;

import lombok.Data;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
public abstract class BungeeConfigAdapter implements ConfigAdapter {
    private File configFile;
    private InputStream inputStream;
    private Configuration configuration;

    public BungeeConfigAdapter(File configFile) {
        this.configFile = configFile;
    }

    public BungeeConfigAdapter(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public static BungeeConfigAdapter create(File configFile) {
        return new BungeeConfigAdapter(configFile) {
        };
    }

    public static BungeeConfigAdapter create(InputStream inputStream) {
        return new BungeeConfigAdapter(inputStream) {
        };
    }

    @Override
    public void load() {
        if (inputStream != null) {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(inputStream);
            return;
        }

        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<String> getKeys(String path) {
        return path == null || path.isBlank() ? configuration.getKeys() : configuration.getSection(path).getKeys();
    }

    @Override
    public void save() {
        if (configFile == null) {
            return;
        }

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
    public long getLong(String key) {
        return configuration.getLong(key);
    }

    @Override
    public long getLong(String key, long def) {
        return configuration.getLong(key, def);
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
    public List<Map<?, ?>> getMapList(String key) {
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
