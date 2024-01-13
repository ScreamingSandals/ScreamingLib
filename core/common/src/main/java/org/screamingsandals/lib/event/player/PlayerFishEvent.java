/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.event.player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.event.PlatformEvent;
import org.screamingsandals.lib.event.CancellableEvent;

import java.util.Locale;

public interface PlayerFishEvent extends CancellableEvent, PlayerEvent, PlatformEvent {

    @Nullable Entity caughtEntity();

    int exp();

    void exp(int exp);

    @NotNull State state();

    @NotNull Entity hookEntity();

    /**
     * An enum to specify the state of the fishing
     */
    // TODO: holder?
    enum State {

        /**
         * When a player is fishing, ie casting the line out.
         */
        FISHING,
        /**
         * When a player has successfully caught a fish and is reeling it in. In
         * this instance, a "fish" is any item retrieved from water as a result
         * of fishing, ie an item, but not necessarily a fish.
         */
        CAUGHT_FISH,
        /**
         * When a player has successfully caught an entity. This refers to any
         * already spawned entity in the world that has been hooked directly by
         * the rod.
         */
        CAUGHT_ENTITY,
        /**
         * When a bobber is stuck in the ground.
         */
        IN_GROUND,
        /**
         * When a player fails to catch a bite while fishing usually due to
         * poor timing.
         */
        FAILED_ATTEMPT,
        /**
         * When a player reels in their hook without receiving any bites.
         */
        REEL_IN,
        /**
         * Called when there is a bite on the hook and it is ready to be reeled
         * in.
         */
        BITE;

        public static @NotNull State convert(@NotNull String state) {
            try {
                return State.valueOf(state.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ex) {
                return State.FISHING;
            }
        }
    }
}
