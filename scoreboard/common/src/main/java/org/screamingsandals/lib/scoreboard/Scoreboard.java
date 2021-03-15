package org.screamingsandals.lib.scoreboard;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

public interface Scoreboard {

    /**
     * @return UUID of this Scoreboard
     */
    UUID getUuid();

    /**
     * @return list of active viewers
     */
    List<PlayerWrapper> getViewers();

    /**
     * Adds new viewer into this Scoreboard
     *
     * @param player new viewer
     * @return this Scoreboard
     */
    Scoreboard addViewer(PlayerWrapper player);

    /**
     * Removes given viewer from this Scoreboard
     *
     * @param player      viewer to remove
     * @param sendPackets if we should send destroy packets
     * @return this Scoreboard
     */
    Scoreboard removeViewer(PlayerWrapper player, boolean sendPackets);

    /**
     * Removes given viewer from this Scoreboard
     *
     * @param player viewer to remove
     * @return this Scoreboard
     */
    default Scoreboard removeViewer(PlayerWrapper player) {
        return removeViewer(player, false);
    }

    /**
     * Checks if this Scoreboard has any viewers.
     *
     * @return true if Scoreboard has any viewers
     */
    boolean hasViewers();

    /**
     * Checks if this Scoreboard is shown to viewers.
     *
     * @return true if is shown to viewers
     */
    boolean isShown();

    /**
     * Shows this Scoreboard to viewers.
     *
     * @return this Scoreboard
     */
    Scoreboard show();

    /**
     * Hides this Scoreboard from viewers;
     *
     * @return this Scoreboard
     */
    Scoreboard hide();

    /**
     * Destroy this Scoreboard.
     */
    void destroy();

    /**
     * This is copy of the original data.
     * Changing this will have no effects on the actual Scoreboard.
     *
     * @return a new {@link TreeMap} with lines.
     */
    TreeMap<Integer, Component> getLines();

}
