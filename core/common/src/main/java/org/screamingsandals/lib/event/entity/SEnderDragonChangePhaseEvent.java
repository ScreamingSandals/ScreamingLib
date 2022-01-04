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

package org.screamingsandals.lib.event.entity;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SEnderDragonChangePhaseEvent extends SCancellableEvent, PlatformEventWrapper {

    EntityBasic getEntity();

    @Nullable
    Phase getCurrentPhase();

    Phase getNewPhase();

    void setNewPhase(Phase newPhase);

    /**
     * Represents a phase or action that an Ender Dragon can perform.
     */
    // TODO: holder?
    enum Phase {
        /**
         * The dragon will circle outside the ring of pillars if ender
         * crystals remain or inside the ring if not.
         */
        CIRCLING,
        /**
         * The dragon will fly towards a targeted player and shoot a
         * fireball when within 64 blocks.
         */
        STRAFING,
        /**
         * The dragon will fly towards the empty portal (approaching
         * from the other side, if applicable).
         */
        FLY_TO_PORTAL,
        /**
         * The dragon will land on on the portal. If the dragon is not near
         * the portal, it will fly to it before mounting.
         */
        LAND_ON_PORTAL,
        /**
         * The dragon will leave the portal.
         */
        LEAVE_PORTAL,
        /**
         * The dragon will attack with dragon breath at its current location.
         */
        BREATH_ATTACK,
        /**
         * The dragon will search for a player to attack with dragon breath.
         * If no player is close enough to the dragon for 5 seconds, the
         * dragon will charge at a player within 150 blocks or will take off
         * and begin circling if no player is found.
         */
        SEARCH_FOR_BREATH_ATTACK_TARGET,
        /**
         * The dragon will roar before performing a breath attack.
         */
        ROAR_BEFORE_ATTACK,
        /**
         * The dragon will charge a player.
         */
        CHARGE_PLAYER,
        /**
         * The dragon will fly to the vicinity of the portal and die.
         */
        DYING,
        /**
         * The dragon will hover at its current location, not performing any actions.
         */
        HOVER
    }
}
