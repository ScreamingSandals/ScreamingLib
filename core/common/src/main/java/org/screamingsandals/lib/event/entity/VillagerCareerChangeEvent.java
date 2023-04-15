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

public interface VillagerCareerChangeEvent extends CancellableEvent, PlatformEvent {

    @NotNull Entity entity();

    @NotNull Profession profession();

    void profession(@NotNull Profession profession);

    @NotNull ChangeReason changeReason();

    /**
     * Reasons for the villager's profession changing.
     */
    // TODO: holder?
    enum ChangeReason {

        /**
         * Villager lost their job due to too little experience.
         */
        LOSING_JOB,
        /**
         * Villager gained employment.
         */
        EMPLOYED
    }

    /**
     * Represents the various different Villager professions there may be.
     * Villagers have different trading options depending on their profession,
     */
    // TODO: holder
    enum Profession {
        NONE,
        /**
         * Armorer profession. Wears a black apron. Armorers primarily trade for
         * iron armor, chainmail armor, and sometimes diamond armor.
         */
        ARMORER,
        /**
         * Butcher profession. Wears a white apron. Butchers primarily trade for
         * raw and cooked food.
         */
        BUTCHER,
        /**
         * Cartographer profession. Wears a white robe. Cartographers primarily
         * trade for explorer maps and some paper.
         */
        CARTOGRAPHER,
        /**
         * Cleric profession. Wears a purple robe. Clerics primarily trade for
         * rotten flesh, gold ingot, redstone, lapis, ender pearl, glowstone,
         * and bottle o' enchanting.
         */
        CLERIC,
        /**
         * Farmer profession. Wears a brown robe. Farmers primarily trade for
         * food-related items.
         */
        FARMER,
        /**
         * Fisherman profession. Wears a brown robe. Fisherman primarily trade
         * for fish, as well as possibly selling string and/or coal.
         */
        FISHERMAN,
        /**
         * Fletcher profession. Wears a brown robe. Fletchers primarily trade
         * for string, bows, and arrows.
         */
        FLETCHER,
        /**
         * Leatherworker profession. Wears a white apron. Leatherworkers
         * primarily trade for leather, and leather armor, as well as saddles.
         */
        LEATHERWORKER,
        /**
         * Librarian profession. Wears a white robe. Librarians primarily trade
         * for paper, books, and enchanted books.
         */
        LIBRARIAN,
        /**
         * Mason profession.
         */
        MASON,
        /**
         * Nitwit profession. Wears a green apron, cannot trade. Nitwit
         * villagers do not do anything. They do not have any trades by default.
         */
        NITWIT,
        /**
         * Sheperd profession. Wears a brown robe. Shepherds primarily trade for
         * wool items, and shears.
         */
        SHEPHERD,
        /**
         * Toolsmith profession. Wears a black apron. Tool smiths primarily
         * trade for iron and diamond tools.
         */
        TOOLSMITH,
        /**
         * Weaponsmith profession. Wears a black apron. Weapon smiths primarily
         * trade for iron and diamond weapons, sometimes enchanted.
         */
        WEAPONSMITH
    }
}
