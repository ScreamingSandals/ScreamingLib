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

package org.screamingsandals.lib.event.entity;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.event.PlatformEvent;
import org.screamingsandals.lib.event.CancellableEvent;

public interface EntityRegainHealthEvent extends CancellableEvent, PlatformEvent {
    @NotNull Entity entity();

    @NotNull RegainReason remainingReason();

    double amount();

    void amount(double amount);

    /**
     * An enum to specify the type of health regaining that is occurring
     */
    // TODO: holder?
    enum RegainReason {

        /**
         * When a player regains health from regenerating due to Peaceful mode
         * (difficulty=0)
         */
        REGEN,
        /**
         * When a player regains health from regenerating due to their hunger
         * being satisfied
         */
        SATIATED,
        /**
         * When a player regains health from eating consumables
         */
        EATING,
        /**
         * When an ender dragon regains health from an ender crystal
         */
        ENDER_CRYSTAL,
        /**
         * When a player is healed by a potion or spell
         */
        MAGIC,
        /**
         * When a player is healed over time by a potion or spell
         */
        MAGIC_REGEN,
        /**
         * When a wither is filling its health during spawning
         */
        WITHER_SPAWN,
        /**
         * When an entity is damaged by the Wither potion effect
         */
        WITHER,
        /**
         * Any other reason not covered by the reasons above
         */
        CUSTOM
    }
}
