package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SPlayerItemHeldEvent extends SPlayerCancellableEvent {
    private final ImmutableObjectLink<Integer> previousSlot;
    private final ImmutableObjectLink<Integer> newSlot;

    public SPlayerItemHeldEvent(ImmutableObjectLink<PlayerWrapper> player,
                                ImmutableObjectLink<Integer> previousSlot,
                                ImmutableObjectLink<Integer> newSlot) {
        super(player);
        this.previousSlot = previousSlot;
        this.newSlot = newSlot;
    }

    /**
     * Gets the previous held slot index
     *
     * @return Previous slot index
     */
    public int getPreviousSlot() {
        return previousSlot.get();
    }

    /**
     * Gets the new held slot index
     *
     * @return New slot index
     */
    public int getNewSlot() {
        return newSlot.get();
    }
}
