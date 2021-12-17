package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SEntityExhaustionEvent extends SCancellableEvent, PlatformEventWrapper {

    EntityBasic getEntity();

    ExhaustionReason getExhaustionReason();

    float getExhaustion();

    void setExhaustion(float exhaustion);

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
