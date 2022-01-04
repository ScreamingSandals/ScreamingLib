/*
 * Copyright 2022 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.hologram;

import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.Pair;
import org.screamingsandals.lib.visuals.DatableVisual;
import org.screamingsandals.lib.visuals.LinedVisual;
import org.screamingsandals.lib.visuals.TouchableVisual;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Map;

/**
 * Hologram that shows some text.
 */
public interface Hologram extends LinedVisual<Hologram>, DatableVisual<Hologram>, TouchableVisual<Hologram> {
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
     * @return current rotation time
     */
    Pair<Integer, TaskerTime> getRotationTime();

    /**
     * The rotation time settings.
     *
     * @param rotatingTime {@link Pair} of time and unit.
     * @return this hologram
     */
    Hologram setRotationTime(Pair<Integer, TaskerTime> rotatingTime);

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
    Hologram setRotationMode(RotationMode mode);

    /**
     * Stands for how much should this hologram rotate per one cycle
     *
     * @param toIncrement
     * @return
     */
    Hologram setRotationIncrement(float toIncrement);

    /**
     * Changes the item to show
     *
     * @param item item to show
     * @return this hologram
     */
    Hologram setItem(Item item);

    /**
     * Position of the item shown as hologram
     *
     * @param position position of the item
     * @return this hologram
     */
    Hologram setItemPosition(ItemPosition position);

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
