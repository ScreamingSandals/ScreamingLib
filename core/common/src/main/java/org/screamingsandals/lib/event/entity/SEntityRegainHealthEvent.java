package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SEntityRegainHealthEvent extends CancellableAbstractEvent {
    private final EntityBasic entity;
    private double amount;
    private final RegainReason regainReason;

    /**
     * An enum to specify the type of health regaining that is occurring
     */
    public enum RegainReason {

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
