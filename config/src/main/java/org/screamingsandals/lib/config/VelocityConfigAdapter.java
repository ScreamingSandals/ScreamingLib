package org.screamingsandals.lib.config;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import lombok.Data;

import java.io.File;
import java.io.InputStream;
import java.util.*;

/* Because Toml class is not editable, so we work with map instead of Toml class */
@Data
public abstract class VelocityConfigAdapter implements ConfigAdapter {
    private File configFile;
    private InputStream inputStream;
    private Map<String, Object> configuration = new HashMap<>();
    private TomlWriter tomlWriter = new TomlWriter();

    public VelocityConfigAdapter(File configFile) {
        this.configFile = configFile;
    }

    public VelocityConfigAdapter(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public static VelocityConfigAdapter create(File configFile) {
        return new VelocityConfigAdapter(configFile) {
        };
    }

    public static VelocityConfigAdapter create(InputStream inputStream) {
        return new VelocityConfigAdapter(inputStream) {
        };
    }

    @Override
    public void load() {
        if (inputStream != null) {
            configuration = new Toml().read(inputStream).toMap();
            return;
        }

        try {
            configuration = new Toml().read(configFile).toMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<String> getKeys(String path) {
        return path == null || path.isBlank() ? configuration.keySet() : ((Map<String, Object>) internalGet(path)).keySet();
    }

    @Override
    public void save() {
        if (configFile == null) {
            return;
        }

        try {
            tomlWriter.write(configuration, configFile);
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object get(String key) {
        return internalGet(key);
    }

    @Override
    public String getString(String key) {
        return (String) internalGet(key);
    }

    @Override
    public String getString(String key, String def) {
        return (String) configuration.getOrDefault(key, def);
    }

    @Override
    public boolean getBoolean(String key) {
        return (boolean) configuration.get(key);
    }

    @Override
    public boolean getBoolean(String key, boolean def) {
        return (boolean) configuration.getOrDefault(key, def);
    }

    @Override
    public int getInt(String key) {
        return ((Long) configuration.get(key)).intValue();
    }

    @Override
    public int getInt(String key, int def) {
        return ((Long) configuration.getOrDefault(key, (long) def)).intValue();
    }

    @Override
    public long getLong(String key) {
        return (long) configuration.get(key);
    }

    @Override
    public long getLong(String key, long def) {
        return (long) configuration.getOrDefault(key, def);
    }

    @Override
    public double getDouble(String key) {
        return (double) configuration.get(key);
    }

    @Override
    public double getDouble(String key, double def) {
        return (double) configuration.getOrDefault(key, def);
    }

    @Override
    public List<Object> getList(String key) {
        return (List<Object>) configuration.get(key);
    }

    @Override
    public List<String> getStringList(String key) {
        return (List<String>) configuration.get(key);
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
        return configuration.containsKey(key);
    }

    @Override
    public void set(String key, Object obj) {
        configuration.put(key, obj);
    }

    /* From TOML */
    private Object internalGet(String key) {
        if (configuration.containsKey(key)) {
            return configuration.get(key);
        }

        Object current = new HashMap<>(configuration);

        String[] keys = key.split("\\.");

        for (String k : keys) {
            if (current instanceof Map) {
                if (((Map<String, Object>) current).containsKey(k)) {
                    return ((Map<String, Object>) current).get(k);
                } else {
                    current = ((Map<String, Object>) current).get(k);
                }
            } else {
                break;
            }
        }

        return null;
    }
}
