package org.screamingsandals.lib.config;

import lombok.Data;

import java.util.*;

@Data
public abstract class HashMapConfigAdapter implements ConfigAdapter {
    private Map<String, Object> configuration = new HashMap<>();

    @Override
    public Collection<String> getKeys(String path) {
        return path == null || path.isBlank() ? configuration.keySet() : ((Map<String, Object>) internalGet(path)).keySet();
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
        return (String) internalGet(key, def);
    }

    @Override
    public boolean getBoolean(String key) {
        return (boolean) internalGet(key, false);
    }

    @Override
    public boolean getBoolean(String key, boolean def) {
        return (boolean) internalGet(key, def);
    }

    @Override
    public int getInt(String key) {
        return ((Long) internalGet(key,0)).intValue();
    }

    @Override
    public int getInt(String key, int def) {
        return ((Long) internalGet(key, (long) def)).intValue();
    }

    @Override
    public long getLong(String key) {
        return (long) internalGet(key,0);
    }

    @Override
    public long getLong(String key, long def) {
        return (long) internalGet(key, def);
    }

    @Override
    public double getDouble(String key) {
        return (double) internalGet(key, 0);
    }

    @Override
    public double getDouble(String key, double def) {
        return (double) internalGet(key, def);
    }

    @Override
    public List<Object> getList(String key) {
        return (List<Object>) internalGet(key);
    }

    @Override
    public List<String> getStringList(String key) {
        return (List<String>) internalGet(key);
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
        return internalGet(key) != null;
    }

    @Override
    public void set(String key, Object obj) {
        internalPut(key, obj);
    }

    /* From HashMap */
    private Object internalGet(String key) {
        return internalGet(key, null);
    }

    private Object internalGet(String key, Object defaultValue) {
        if (configuration.containsKey(key)) {
            return configuration.get(key);
        }

        String[] keys = key.split("\\.");

        Map<String,Object> map = new HashMap<>(configuration);

        for (var i = 0; i < (keys.length - 1); i++) {
            map = (Map<String,Object>) map.get(keys[i]);
            if (map == null) {
                return defaultValue;
            }
        }

        return map.getOrDefault(key, defaultValue);
    }

    private void internalPut(String key, Object value) {
        String[] keys = key.split("\\.");

        var map = configuration;

        for (var i = 0; i < (keys.length - 1); i++) {
            map = (Map<String,Object>) map.computeIfAbsent(keys[i], k -> new HashMap<>());
        }

        map.put(keys[keys.length - 1], value);
    }
}
