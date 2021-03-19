package org.screamingsandals.lib.utils.data;

import java.util.Map;
import java.util.Optional;

public interface DataContainer {

    /**
     * @return New data container.
     */
    static DataContainer get() {
        return SimpleDataContainer.get();
    }

    /**
     * Immutable copy of the data in this hologram.
     *
     * @return copy of data that this hologram has.
     */
    Map<String, Object> getAll();

    <T> T get(String key);

    <T> Optional<T> getOptional(String key);

    boolean contains(String key);

    boolean isEmpty();

    void set(String key, Object data);

    void add(String key, Object data);
}
