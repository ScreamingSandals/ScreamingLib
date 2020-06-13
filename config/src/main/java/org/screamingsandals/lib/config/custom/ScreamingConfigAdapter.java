package org.screamingsandals.lib.config.custom;

import org.screamingsandals.lib.debug.Debug;

import java.util.HashMap;
import java.util.Map;

public abstract class ScreamingConfigAdapter implements ScreamingConfig{
    private final Map<String, ValueHolder<?>> values = new HashMap<>();

    @Override
    public void registerValue(ValueHolder<?> value) {
        values.putIfAbsent(value.getKey(), value);
    }

    @Override
    public void unregisterValue(String key) {
        values.remove(key);
    }

    @Override
    public void unregisterValue(ValueHolder<?> value) {
        unregisterValue(value.getKey());
    }

    @Override
    public void replaceValue(ValueHolder<?> value) {
        final var key = value.getKey();

        values.remove(key);
        values.put(key, value);
    }

    @Override
    public ValueHolder<?> getValueHolder(String key) {
        return values.get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ValueHolder<T>> T get(String key) {
        if (!values.containsKey(key)) {
            return null;
        }
        try {
            return (T) values.get(key).getValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ValueHolder<T>> T get(String key, T def) {
        if (!values.containsKey(key)) {
            Debug.warn("It does not exists");
            return def;
        }
        try {
            return (T) values.get(key).getValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    @Override
    public Map<String, ValueHolder<?>> getValues() {
        return new HashMap<>(values);
    }
}
