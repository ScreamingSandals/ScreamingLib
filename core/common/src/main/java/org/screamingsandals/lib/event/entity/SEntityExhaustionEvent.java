package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SEntityExhaustionEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<EntityBasic> entity;
    private final ImmutableObjectLink<ExhaustionReason> exhaustionReason;
    private final ObjectLink<Float> exhaustion;

    public EntityBasic getEntity() {
        return entity.get();
    }

    public ExhaustionReason getExhaustionReason() {
        return exhaustionReason.get();
    }

    public float getExhaustion() {
        return exhaustion.get();
    }

    public void setExhaustion(float exhaustion) {
        this.exhaustion.set(exhaustion);
    }

    /**
     * The reason for why a PlayerExhaustionEvent takes place
     */
    // TODO: holder?
    public enum ExhaustionReason {

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
