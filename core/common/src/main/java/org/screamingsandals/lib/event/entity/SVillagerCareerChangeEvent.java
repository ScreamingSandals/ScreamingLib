package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SVillagerCareerChangeEvent extends CancellableAbstractEvent {
    private final EntityBasic entity;
    private Profession profession;
    private final ChangeReason reason;

    /**
     * Reasons for the villager's profession changing.
     */
    public enum ChangeReason {

        /**
         * Villager lost their job due to too little experience.
         */
        LOSING_JOB,
        /**
         * Villager gained employment.
         */
        EMPLOYED;
    }

    /**
     * Represents the various different Villager professions there may be.
     * Villagers have different trading options depending on their profession,
     */
    public enum Profession {
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
        WEAPONSMITH;
    }
}
