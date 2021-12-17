package org.screamingsandals.lib.event.player;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SPlayerFishEvent extends SCancellableEvent, SPlayerEvent, PlatformEventWrapper {

    @Nullable
    EntityBasic getEntity();

    int getExp();

    void setExp(int exp);

    State getState();

    EntityBasic getHookEntity();

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

        public static State convert(String state) {
            try {
                return State.valueOf(state.toUpperCase());
            } catch (IllegalArgumentException ex) {
                return State.FISHING;
            }
        }
    }
}
