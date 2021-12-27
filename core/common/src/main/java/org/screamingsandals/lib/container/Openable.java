package org.screamingsandals.lib.container;

import org.screamingsandals.lib.player.PlayerWrapper;

/**
 * An interface representing an openable inventory.
 */
public interface Openable {
    /**
     * Opens this inventory to the supplied player.
     *
     * @param wrapper the player
     */
    void openInventory(PlayerWrapper wrapper);
}
