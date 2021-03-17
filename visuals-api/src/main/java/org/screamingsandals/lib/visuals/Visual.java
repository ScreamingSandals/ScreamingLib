package org.screamingsandals.lib.visuals;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.Collection;
import java.util.UUID;

/**
 * Represents Visual that can have title and viewers.
 */
public interface Visual<T> {

    /**
     * Each visual can have its UUID.
     *
     * @return UUID of this visual.
     */
    UUID uuid();

    /**
     * @return viewers that are currently looking and this visual.
     */
    Collection<PlayerWrapper> viewers();

    T update();

    T show();

    T hide();

    T addViewer(PlayerWrapper viewer);

    T newViewer(PlayerWrapper player);

    T removeViewer(PlayerWrapper player);

    T clearViewers();

    T title(Component title);

    /**
     * Checks if this Visual has any viewers.
     *
     * @return true if yes, duh.
     */
    boolean hasViewers();

    boolean isShown();

    void destroy();

}
