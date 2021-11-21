package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.SCancellableEvent;

public interface SPlayerItemHeldEvent extends SCancellableEvent, SPlayerEvent {

    /**
     * Gets the previous held slot index
     *
     * @return Previous slot index
     */
    int getPreviousSlot();

    /**
     * Gets the new held slot index
     *
     * @return New slot index
     */
    int getNewSlot();
}
