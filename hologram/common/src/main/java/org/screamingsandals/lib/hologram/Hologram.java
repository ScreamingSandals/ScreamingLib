package org.screamingsandals.lib.hologram;

import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents an invisible entity with only some parts visible - e.g. Hologram.
 */
public interface Hologram {
    /**
     * This is default view distance SQUARED!
     * <p>
     * That means that at this distance, you will see the hologram from 64 blocks away.
     */
    int DEFAULT_VIEW_DISTANCE = 4096;

    /**
     * @return UUID of this Hologram
     */
    UUID getUuid();

    /**
     * @return list of active viewers
     */
    List<PlayerWrapper> getViewers();

    /**
     * Adds new viewer into this Hologram
     *
     * @param player new viewer
     * @return this hologram
     */
    Hologram addViewer(PlayerWrapper player);

    /**
     * Removes given viewer from this Hologram
     *
     * @param player viewer to remove
     * @return this hologram
     */
    Hologram removeViewer(PlayerWrapper player);

    /**
     * Checks if this Hologram has any viewers.
     *
     * @return true if Hologram has any viewers
     */
    boolean hasViewers();

    /**
     * Location of this hologram
     *
     * @return {@link Optional} of Hologram location
     */
    Optional<LocationHolder> getLocation();

    /**
     * Sets new location for this Hologram
     *
     * @param location new location
     * @return this hologram
     */
    Hologram setLocation(LocationHolder location);

    /**
     * @return current view distance SQUARED
     */
    int getViewDistance();

    /**
     * Sets new view distance for this hologram.
     * !!!! NEEDS TO BE SQUARED !!!!
     *
     * @param viewDistance new view distance
     * @return this hologram
     */
    Hologram setViewDistance(int viewDistance);

    /**
     * Checks if you can interact with this Hologram
     *
     * @return true if this hologram is touchable
     */
    boolean isTouchable();

    /**
     * Changes interact state for this hologram
     *
     * @param touchable touchable state
     * @return this hologram
     */
    Hologram setTouchable(boolean touchable);

    /**
     * Checks if this hologram is shown to viewers.
     *
     * @return true if is shown to viewers
     */
    boolean isShown();

    /**
     * Shows this Hologram to viewers.
     *
     * @return this hologram
     */
    Hologram show();

    /**
     * Hides this Hologram from viewers;
     *
     * @return this hologram
     */
    Hologram hide();

    /**
     * Destroy this hologram.
     */
    void destroy();

    /**
     * @return data stored in this hologram
     */
    Data getData();

    /**
     * Replaces current data with new one
     *
     * @param data new data
     */
    void newData(Data data);

    /**
     * Data storage for given hologram
     */
    interface Data {
        /**
         * Immutable copy of the data in this hologram.
         * @return copy of data that this hologram has.
         */
        Map<String, Object> getAll();

        <T> T get(String key);

        boolean contains(String key);

        void set(String key, Object data);

        void add(String key, Object data);
    }
}
