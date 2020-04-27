package org.screamingsandals.gamecore.config;

import org.screamingsandals.lib.config.ConfigAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameConfig implements ConfigAdapter {
    private Map<String, Object> gameValues = new HashMap<>();

    public void buildDefaults() {

    }

    public interface DefaultKey {
        String GAME_TIME = "game_time";
    }

    public void registerValue(String key, Object value) {
        gameValues.putIfAbsent(key, value);
    }

    public void unregisterValue(String key) {
        gameValues.remove(key);
    }

    @Override
    public Object get(String key) {
        return gameValues.get(key);
    }

    @Override
    public String getString(String key) {
        final var value = gameValues.get(key);

        if (value instanceof String) {
            return (String) value;
        }

        return null;
    }

    @Override
    public String getString(String key, String def) {
        final var value = gameValues.get(key);

        if (value instanceof String) {
            return (String) value;
        }

        return def;
    }

    @Override
    public boolean getBoolean(String key) {
        final var value = gameValues.get(key);

        if (value instanceof Boolean) {
            return (Boolean) value;
        }

        return false;
    }

    @Override
    public boolean getBoolean(String key, boolean def) {
        final var value = gameValues.get(key);

        if (value instanceof Boolean) {
            return (Boolean) value;
        }

        return def;
    }

    @Override
    public int getInt(String key) {
        final var value = gameValues.get(key);

        if (value instanceof Integer) {
            return (Integer) value;
        }

        return 0;
    }

    @Override
    public int getInt(String key, int def) {
        final var value = gameValues.get(key);

        if (value instanceof Integer) {
            return (Integer) value;
        }

        return def;
    }

    @Override
    public double getDouble(String key) {
        final var value = gameValues.get(key);

        if (value instanceof Double) {
            return (Double) value;
        }

        return 0;
    }

    @Override
    public double getDouble(String key, double def) {
        final var value = gameValues.get(key);

        if (value instanceof Double) {
            return (Double) value;
        }

        return def;
    }

    @Override
    public List<?> getList(String key) {
        final var value = gameValues.get(key);

        if (value instanceof List<?>) {
            return (List<?>) value;
        }

        return null;
    }

    @Override
    public List<Map<?, ?>> getMap(String key) {
        final var value = gameValues.get(key);

        if (value instanceof List<?>) {
            return (List<Map<?, ?>>) value;
        }

        return null;
    }

    @Override
    public List<String> getStringList(String key) {
        final var value = gameValues.get(key);

        if (value instanceof List<?>) {
            return (List<String>) value;
        }

        return null;
    }

    @Override
    public boolean isSet(String key) {
        final var value = gameValues.get(key);

        return value != null;
    }

    @Override
    public void set(String key, Object obj) {
        final var value = gameValues.get(key);

        if (value != null) {
            unregisterValue(key);
        }

        registerValue(key, obj);
    }

    @Override
    public void save() {
        //not used
    }

    @Override
    public void load() {
        //not used
    }
}
