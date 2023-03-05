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
import org.screamingsandals.lib.entity.BasicEntity;
import org.screamingsandals.lib.event.PlatformEvent;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface EntityExhaustionEvent extends SCancellableEvent, PlatformEvent {

    @NotNull BasicEntity entity();

    @NotNull ExhaustionReason exhaustionReason();

    float exhaustion();

    void exhaustion(float exhaustion);

    /**
     * The reason for why a PlayerExhaustionEvent takes place
     */
    // TODO: holder?
    enum ExhaustionReason {

        /**
         * Player mines a block
         */
        BLOCK_MINED,
        /**
         * Player has the hunger potion effect
         */
        HUNGER_EFFECT,
        /**
         * Player takes damage
         */
        DAMAGED,
        /**
         * Player attacks another entity
         */
        ATTACK,
        /**
         * Player is sprint jumping
         */
        JUMP_SPRINT,
        /**
         * Player jumps
         */
        JUMP,
        /**
         * Player swims one centimeter
         */
        SWIM,
        /**
         * Player walks underwater one centimeter
         */
        WALK_UNDERWATER,
        /**
         * Player moves on the surface of water one centimeter
         */
        WALK_ON_WATER,
        /**
         * Player sprints one centimeter
         */
        SPRINT,
        /**
         * Player crouches one centimeter (does not effect exhaustion, but fires
         * nonetheless)
         */
        CROUCH,
        /**
         * Player walks one centimeter (does not effect exhaustion, but fires
         * nonetheless)
         */
        WALK,
        /**
         * Player regenerated health
         */
        REGEN,
        /**
         * Unknown exhaustion reason
         */
        UNKNOWN
    }
}
