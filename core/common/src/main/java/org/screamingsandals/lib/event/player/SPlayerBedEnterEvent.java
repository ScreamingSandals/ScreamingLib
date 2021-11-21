package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.block.BlockHolder;

public interface SPlayerBedEnterEvent extends SCancellableEvent, SPlayerEvent {
    /**
     * Represents the default possible outcomes of this event.
     */
    // TODO: holder?
    enum BedEnterResult {
        /**
         * The player will enter the bed.
         */
        OK,
        /**
         * The world doesn't allow sleeping (ex. Nether or The End). Entering
         * the bed is prevented and the bed explodes.
         */
        NOT_POSSIBLE_HERE,
        /**
         * Entering the bed is prevented due to it not being night nor
         * thundering currently.
         * <p>
         * If the event is forcefully allowed during daytime, the player will
         * enter the bed (and set its bed location), but might get immediately
         * thrown out again.
         */
        NOT_POSSIBLE_NOW,
        /**
         * Entering the bed is prevented due to the player being too far away.
         */
        TOO_FAR_AWAY,
        /**
         * Entering the bed is prevented due to there being monsters nearby.
         */
        NOT_SAFE,
        /**
         * Entering the bed is prevented due to there being some other problem.
         */
        OTHER_PROBLEM;

        public static BedEnterResult convert(String cause) {
            try {
                return BedEnterResult.valueOf(cause.toUpperCase());
            } catch (IllegalArgumentException ex) {
                return BedEnterResult.OK;
            }
        }
    }


    BlockHolder getBed();

    BedEnterResult getBedEnterResult();

    Result getUseBed();

    void setUseBed(Result useBed);

    @Override
    default boolean isCancelled() {
        return (getUseBed() == Result.DENY || getUseBed() == Result.DEFAULT && getBedEnterResult() != BedEnterResult.OK);
    }

    @Override
    default void setCancelled(boolean cancelled) {
        setUseBed(cancelled ? Result.DENY: Result.DEFAULT);
    }
}
