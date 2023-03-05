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

package org.screamingsandals.lib.visuals;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.world.Location;

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
    int viewDistance();

    @Contract("_ -> this")
    @NotNull T viewDistance(int viewDistance);

    @NotNull Location location();

    @Contract("_ -> this")
    @NotNull T location(@NotNull Location location);

    /**
     * Spawns the visual to all visible Players.
     * @return this visual
     */
    @Contract("-> this")
    @NotNull T spawn();

    boolean created();

}
