package org.screamingsandals.lib.config;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public interface ConfigAdapter {
    /**
     * @param key key to value in file
     * @return Object of that value or null
     */
    Object get(String key);

    /**
     * @param key key to value in file
     * @return String of that value or null
     */
    String getString(String key);

    /**
     * @param key key to value in file
     * @param def default value
     * @return String of that value or default value
     */
    String getString(String key, String def);

    /**
     * @param key key to value in file
     * @return boolean of that value or null
     */
    boolean getBoolean(String key);

    /**
     * @param key key to value in file
     * @param def default value
     * @return boolean of that value or default value
     */
    boolean getBoolean(String key, boolean def);

    /**
     * @param key key to value in file
     * @return int of that value or null
     */
    int getInt(String key);

    /**
     * @param key key to value in file
     * @param def default value
     * @return int of that value or default value
     */
    int getInt(String key, int def);

    /**
     * @param key key to value in file
     * @return double of that value or null
     */
    double getDouble(String key);

    /**
     * @param key key to value in file
     * @param def default value
     * @return double of that value or default value
     */
    double getDouble(String key, double def);

    /**
     * @param key key to value in file
     * @return List of that value or null
     */
    List<?> getList(String key);

    /**
     * @param key key to value in file
     * @return List of that value or null
     */
    List<Map<?, ?>> getMap(String key);

    /**
     * @param key key to value in file
     * @return String list of that value or null
     */
    List<String> getStringList(String key);

    /**
     * @param key key to value in file
     * @return true if that value exists
     */
    boolean isSet(String key);

    /**
     * sets the value to the config map
     *
     * @param key identifier
     * @param obj value
     */
    void set(String key, Object obj);

    /**
     * Saves the current map to file
     */
    void save();

    /**
     * Loads config to the map from file
     */
    void load();

    default void checkOrSet(AtomicBoolean modify, String path, java.io.Serializable value) {
        if (!isSet(path)) {
            set(path, value);
            modify.set(true);
        }
    }
}
