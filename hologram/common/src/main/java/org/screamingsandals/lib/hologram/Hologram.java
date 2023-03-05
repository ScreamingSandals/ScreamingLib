/*
 * Copyright 2023 ScreamingSandals
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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.Pair;
import org.screamingsandals.lib.visuals.DatableVisual;
import org.screamingsandals.lib.visuals.LinedVisual;
import org.screamingsandals.lib.visuals.TouchableVisual;
import org.screamingsandals.lib.world.Location;

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
    @Contract("_ -> new")
    static @NotNull Hologram of(@NotNull Location location) {
        return HologramManager.hologram(location);
    }

    /**
     * Creates new touchable hologram.
     *
     * @param location location where to create the hologram
     * @return created hologram
     */
    @Contract("_ -> new")
    static @NotNull Hologram touchableOf(@NotNull Location location) {
        return HologramManager.hologram(location, true);
    }

    /**
     * @return current rotation time
     */
    @NotNull Pair<@NotNull Integer, @NotNull TaskerTime> rotationTime();

    /**
     * The rotation time settings.
     *
     * @param rotatingTime {@link Pair} of time and unit.
     * @return this hologram
     */
    @Contract("_ -> this")
    @NotNull Hologram rotationTime(@NotNull Pair<@NotNull Integer, @NotNull TaskerTime> rotatingTime);

    /**
     * @return current rotation mode
     */
    @NotNull RotationMode rotationMode();

    /**
     * The mode that the Hologram should be rotating
     *
     * @param mode rotation mode
     * @return this hologram
     */
    @Contract("_ -> this")
    @NotNull Hologram rotationMode(@NotNull RotationMode mode);

    /**
     * Stands for how much should this hologram rotate per one cycle
     *
     * @param toIncrement
     * @return
     */
    @Contract("_ -> this")
    @NotNull Hologram rotationIncrement(float toIncrement);

    /**
     * Changes the item to show
     *
     * @param item item to show
     * @return this hologram
     */
    @Contract("_ -> this")
    @NotNull Hologram item(@Nullable ItemStack item);

    /**
     * Position of the item shown as hologram
     *
     * @param position position of the item
     * @return this hologram
     */
    @Contract("_ -> this")
    @NotNull Hologram itemPosition(@NotNull ItemPosition position);

    /**
     * @return current item position
     */
    @NotNull ItemPosition itemPosition();

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
}
