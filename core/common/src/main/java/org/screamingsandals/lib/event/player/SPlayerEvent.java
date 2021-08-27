package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.player.PlayerWrapper;

public interface SPlayerEvent {

    /**
     * Gets the player associated with this event.
     * @return player that triggered the event
     */
    PlayerWrapper getPlayer();
}
