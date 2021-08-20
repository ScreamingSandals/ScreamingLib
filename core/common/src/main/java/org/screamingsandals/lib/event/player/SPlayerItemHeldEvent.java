package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class SPlayerItemHeldEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ImmutableObjectLink<Integer> previousSlot;
    private final ImmutableObjectLink<Integer> newSlot;

    public PlayerWrapper getPlayer() {
        return player.get();
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
