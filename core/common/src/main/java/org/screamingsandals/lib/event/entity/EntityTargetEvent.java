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

package org.screamingsandals.lib.event.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.event.PlatformEvent;
import org.screamingsandals.lib.event.CancellableEvent;

public interface EntityTargetEvent extends CancellableEvent, PlatformEvent {

    @NotNull Entity entity();

    @Nullable Entity target();

    void target(@Nullable Entity target);

    @NotNull TargetReason targetReason();

    /**
     * An enum to specify the reason for the targeting
     */
    // TODO: holder?
    enum TargetReason {

        /**
         * When the entity's target has died, and so it no longer targets it
         */
        TARGET_DIED,
        /**
         * When the entity doesn't have a target, so it attacks the nearest
         * player
         */
        CLOSEST_PLAYER,
        /**
         * When the target attacks the entity, so entity targets it
         */
        TARGET_ATTACKED_ENTITY,
        /**
         * When the target attacks a fellow pig zombie, so the whole group
         * will target him with this reason.
         *
         * @deprecated obsoleted by {@link #TARGET_ATTACKED_NEARBY_ENTITY}
         */
        @Deprecated
        PIG_ZOMBIE_TARGET,
        /**
         * When the target is forgotten for whatever reason.
         */
        FORGOT_TARGET,
        /**
         * When the target attacks the owner of the entity, so the entity
         * targets it.
         */
        TARGET_ATTACKED_OWNER,
        /**
         * When the owner of the entity attacks the target attacks, so the
         * entity targets it.
         */
        OWNER_ATTACKED_TARGET,
        /**
         * When the entity has no target, so the entity randomly chooses one.
         */
        RANDOM_TARGET,
        /**
         * When an entity selects a target while defending a village.
         */
        DEFEND_VILLAGE,
        /**
         * When the target attacks a nearby entity of the same type, so the entity targets it
         */
        TARGET_ATTACKED_NEARBY_ENTITY,
        /**
         * When a zombie targeting an entity summons reinforcements, so the reinforcements target the same entity
         */
        REINFORCEMENT_TARGET,
        /**
         * When an entity targets another entity after colliding with it.
         */
        COLLISION,
        /**
         * For custom calls to the event.
         */
        CUSTOM,
        /**
         * When the entity doesn't have a target, so it attacks the nearest
         * entity
         */
        CLOSEST_ENTITY,
        /**
         * When a raiding entity selects the same target as one of its compatriots.
         */
        FOLLOW_LEADER,
        /**
         * When another entity tempts this entity by having a desired item such
         * as wheat in its hand.
         */
        TEMPT,
        /**
         * A currently unknown reason for the entity changing target.
         */
        UNKNOWN
    }
}
