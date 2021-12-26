package org.screamingsandals.lib.container;

import org.screamingsandals.lib.player.PlayerWrapper;

/**
 * <p>An interface representing an openable inventory.</p>
 */
public interface Openable {
    /**
     * <p>Opens this inventory to the supplied player.</p>
     *
     * @param wrapper the player
     */
    void openInventory(PlayerWrapper wrapper);
}
