package org.screamingsandals.lib.event.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
@Data
public class SPlayerBedEnterEvent extends CancellableAbstractEvent {
    /**
     * Represents the default possible outcomes of this event.
     */
    public enum BedEnterResult {
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

    private final PlayerWrapper player;
    private final BlockHolder bed;
    private final BedEnterResult bedEnterResult;
    private Result useBed = Result.DEFAULT;

    @Override
    public boolean isCancelled() {
        return (useBed == Result.DENY || useBed == Result.DEFAULT && bedEnterResult != BedEnterResult.OK);
    }
}
