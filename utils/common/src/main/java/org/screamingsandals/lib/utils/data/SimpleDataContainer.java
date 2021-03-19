package org.screamingsandals.lib.utils.data;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class SimpleDataContainer implements DataContainer {
    private final Map<String, Object> dataMap = new ConcurrentHashMap<>();

    static SimpleDataContainer get() {
        return new SimpleDataContainer();
    }

    @Override
    public Map<String, Object> getAll() {
        return Map.copyOf(dataMap);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key) {
        if (dataMap.containsKey(key)) {
            return (T) dataMap.get(key);
        }

        throw new NullPointerException("Data for key " + key + " was not found!");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<T> getOptional(String key) {
        return Optional.ofNullable((T) dataMap.get(key));
    }

    @Override
    public boolean contains(String key) {
        return dataMap.containsKey(key);
    }

    @Override
    public boolean isEmpty() {
        return dataMap.isEmpty();
    }

    @Override
    public void set(String key, Object data) {
        dataMap.put(key, data);
    }

    @Override
    public void add(String key, Object data) {
        dataMap.putIfAbsent(key, data);
    }
}
