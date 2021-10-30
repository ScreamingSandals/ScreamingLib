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
public class SEntityTargetEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<EntityBasic> entity;
    private final ObjectLink<EntityBasic> target;
    private final ImmutableObjectLink<TargetReason> targetReason;

    public EntityBasic getEntity() {
        return entity.get();
    }

    public EntityBasic getTarget() {
        return target.get();
    }

    public void setTarget(EntityBasic target) {
        this.target.set(target);
    }

    public TargetReason getTargetReason() {
        return targetReason.get();
    }

    /**
     * An enum to specify the reason for the targeting
     */
    // TODO: holder?
    public enum TargetReason {

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
        UNKNOWN;
    }
}
