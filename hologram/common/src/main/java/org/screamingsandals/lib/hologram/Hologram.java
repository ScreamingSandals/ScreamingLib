package org.screamingsandals.lib.hologram;

import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.Pair;
import org.screamingsandals.lib.visuals.DatableVisual;
import org.screamingsandals.lib.visuals.LinedVisual;
import org.screamingsandals.lib.visuals.LocatableVisual;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Map;

/**
 * Hologram that shows some text.
 */
public interface Hologram extends LocatableVisual<Hologram>, LinedVisual<Hologram>, DatableVisual<Hologram> {
    /**
     * Default rate that the item is rotated.
     */
    float DEFAULT_ROTATION_INCREMENT = 10f;

    /**
     * Creates new hologram.
     *
     * @param location location where to create the hologram
     * @return created hologram
     */
    static Hologram of(LocationHolder location) {
        return HologramManager.hologram(location);
    }

    /**
     * Creates new touchable hologram.
     *
     * @param location location where to create the hologram
     * @return created hologram
     */
    static Hologram touchableOf(LocationHolder location) {
        return HologramManager.hologram(location, true);
    }

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
     * @return current rotation time
     */
    Pair<Integer, TaskerTime> getRotationTime();

    /**
     * The rotation time settings.
     *
     * @param rotatingTime {@link Pair} of time and unit.
     * @return this hologram
     */
    Hologram rotationTime(Pair<Integer, TaskerTime> rotatingTime);

    /**
     * @return current rotation mode
     */
    RotationMode getRotationMode();

    /**
     * The mode that the Hologram should be rotating
     *
     * @param mode rotation mode
     * @return this hologram
     */
    Hologram rotationMode(RotationMode mode);

    /**
     * Stands for how much should this hologram rotate per one cycle
     *
     * @param toIncrement
     * @return
     */
    Hologram rotationIncrement(float toIncrement);

    /**
     * Changes the item to show
     *
     * @param item item to show
     * @return this hologram
     */
    Hologram item(Item item);

    /**
     * Position of the item shown as hologram
     *
     * @param position position of the item
     * @return this hologram
     */
    Hologram itemPosition(ItemPosition position);

    /**
     * @return current item position
     */
    ItemPosition getItemPosition();

    /**
     * Represents rotation mode
     */
    enum RotationMode {
        X,
        Y,
        Z,
        ALL,
        XY,
        NONE
    }

    /**
     * Represents Item position
     */
    enum ItemPosition {
        ABOVE,
        BELOW
    }

    /**
     * Data storage for given hologram
     */
    interface Data {
        /**
         * Immutable copy of the data in this hologram.
         *
         * @return copy of data that this hologram has.
         */
        Map<String, Object> getAll();

        <T> T get(String key);

        boolean contains(String key);

        void set(String key, Object data);

        void add(String key, Object data);
    }
}
