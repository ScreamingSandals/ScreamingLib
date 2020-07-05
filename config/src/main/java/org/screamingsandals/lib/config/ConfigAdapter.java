package org.screamingsandals.lib.config;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
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
     * @return long of that value or null
     */
    long getLong(String key);

    /**
     * @param key key to value in file
     * @param def default value
     * @return long of that value or default value
     */
    long getLong(String key, long def);

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
    List<Map<?, ?>> getMapList(String key);

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


    /**
     * @return Collection of keys in root path
     */
    default Collection<String> getKeys() {
        return getKeys(null);
    }

    /**
     *
     * @param path Path where we should look for keys
     * @return Collection of keys in specified path
     */
    Collection<String> getKeys(String path);

    /**
     *
     * @param modify AtomicBoolean instance storing information if file should be saved
     * @param path identifier
     * @param value value that will be saved
     * @see DefaultConfigBuilder#start(ConfigAdapter)
     */
    default void checkOrSet(AtomicBoolean modify, String path, Object value) {
        if (!isSet(path)) {
            set(path, value);
            modify.set(true);
        }
    }

    /**
     * Just a little utility method for creating files
     * @param dataFolder folder where to create the file
     * @param fileName filename (config.yml for example)
     * @return the created file
     * @throws IOException if something fucks up
     */
    static File createFile(File dataFolder, String fileName) throws IOException {
        dataFolder.mkdirs();

        File configFile = new File(dataFolder, fileName);
        if (!configFile.exists()) {
            configFile.createNewFile();
        }
        return configFile;
    }
}
