package org.screamingsandals.lib.visuals;

import org.screamingsandals.lib.world.LocationHolder;

public interface LocatableVisual<T> extends Visual<T> {
    /**
     * This is default view distance SQUARED!
     * <p>
     * That means that at this distance, you will see the hologram from 64 blocks away.
     */
    int DEFAULT_VIEW_DISTANCE = 4096;

    /**
     * @return current view distance SQUARED
     */
    int viewDistance();

    T viewDistance(int viewDistance);

    LocationHolder location();

    T location(LocationHolder location);
}
