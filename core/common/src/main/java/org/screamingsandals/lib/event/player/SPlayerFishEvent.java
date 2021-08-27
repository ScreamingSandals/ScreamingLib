package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerFishEvent extends CancellableAbstractEvent implements SPlayerEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ImmutableObjectLink<EntityBasic> entity;
    private final ObjectLink<Integer> exp;
    private final ImmutableObjectLink<State> state;
    private final ImmutableObjectLink<EntityBasic> hookEntity;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    public EntityBasic getEntity() {
        return entity.get();
    }

    public int getExp() {
        return exp.get();
    }

    public void setExp(int exp) {
        this.exp.set(exp);
    }

    public State getState() {
        return state.get();
    }

    public EntityBasic getHookEntity() {
        return hookEntity.get();
    }

    /**
     * An enum to specify the state of the fishing
     */
    // TODO: holder?
    public enum State {

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

        public static State convert(String state) {
            try {
                return State.valueOf(state.toUpperCase());
            } catch (IllegalArgumentException ex) {
                return State.FISHING;
            }
        }
    }
}
