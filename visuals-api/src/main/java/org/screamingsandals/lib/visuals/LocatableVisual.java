package org.screamingsandals.lib.visuals;

import org.screamingsandals.lib.world.LocationHolder;

/**
 * Represents a LocatableVisual that holds a location and can be seen by a Player. Examples:- Hologram, NPC etc.
 */
public interface LocatableVisual<T> extends Visual<T> {

    /**
     * This is default view distance SQUARED!
     * <p>
     * That means that at this distance, you will see the hologram from 64 blocks away.
     */
    int DEFAULT_VIEW_DISTANCE = 4096;

    /**
     * Gets the view distance
     *
     * @return current view distance SQUARED
     */
    int getViewDistance();

    T setViewDistance(int viewDistance);

    LocationHolder getLocation();

    T setLocation(LocationHolder location);

    /**
     * Spawns the visual to all visible Players.
     * @return this visual
     */
    T spawn();

    void destroy();

    boolean isCreated();

    /**
     *
     * @return true if this Visual has been destroyed, false otherwise
     */
    boolean isDestroyed();

}
