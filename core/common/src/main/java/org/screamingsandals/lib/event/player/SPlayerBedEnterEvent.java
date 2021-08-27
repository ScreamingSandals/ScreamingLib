package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = false)
public class SPlayerBedEnterEvent extends SPlayerCancellableEvent {
    /**
     * Represents the default possible outcomes of this event.
     */
    // TODO: holder?
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

    private final ImmutableObjectLink<BlockHolder> bed;
    private final ImmutableObjectLink<BedEnterResult> bedEnterResult;
    private final ObjectLink<Result> useBed;

    public SPlayerBedEnterEvent(ImmutableObjectLink<PlayerWrapper> player,
                                ImmutableObjectLink<BlockHolder> bed,
                                ImmutableObjectLink<BedEnterResult> bedEnterResult,
                                ObjectLink<Result> useBed) {
        super(player);
        this.bed = bed;
        this.bedEnterResult = bedEnterResult;
        this.useBed = useBed;
    }

    public BlockHolder getBed() {
        return bed.get();
    }

    public BedEnterResult getBedEnterResult() {
        return bedEnterResult.get();
    }

    public Result getUseBed() {
        return useBed.get();
    }

    public void setUseBed(Result useBed) {
        this.useBed.set(useBed);
    }

    @Override
    public boolean isCancelled() {
        return (useBed.get() == Result.DENY || useBed.get() == Result.DEFAULT && bedEnterResult.get() != BedEnterResult.OK);
    }
}
