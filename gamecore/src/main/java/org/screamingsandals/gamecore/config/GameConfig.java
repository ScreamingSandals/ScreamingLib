package org.screamingsandals.gamecore.config;

import org.screamingsandals.lib.config.ConfigAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameConfig implements ConfigAdapter {
    private Map<String, Object> gameValues = new HashMap<>();

    public void buildDefaults() {

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
        return null;
    }

    @Override
    public String getString(String key, String def) {
        return null;
    }

    @Override
    public boolean getBoolean(String key) {
        return false;
    }

    @Override
    public boolean getBoolean(String key, boolean def) {
        return false;
    }

    @Override
    public int getInt(String key) {
        return 0;
    }

    @Override
    public int getInt(String key, int def) {
        return 0;
    }

    @Override
    public double getDouble(String key) {
        return 0;
    }

    @Override
    public double getDouble(String key, double def) {
        return 0;
    }

    @Override
    public List<?> getList(String key) {
        return null;
    }

    @Override
    public List<Map<?, ?>> getMap(String key) {
        return null;
    }

    @Override
    public List<String> getStringList(String key) {
        return null;
    }

    @Override
    public boolean isSet(String key) {
        return false;
    }

    @Override
    public void set(String key, Object obj) {

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
