package org.screamingsandals.lib.scoreboard;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.scoreboard.team.ScoreboardTeam;
import org.screamingsandals.lib.utils.visual.TextEntry;

import java.util.*;

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
     * @return a new {@link Map} with lines.
     */
    Map<Integer, TextEntry> getLines();

    /**
     * Replaces the title.
     *
     * @param text text to add
     * @return this Scoreboard
     */
    Scoreboard title(Component text);

    /**
     * Gets the title;
     *
     * @return title
     */
    Component getTitle();

    /**
     * Adds new line to the Scoreboard and moves everything else 1 line down.
     *
     * @param text text to add
     * @return this Scoreboard
     */
    Scoreboard firstLine(TextEntry text);

    /**
     * Adds given collection of new lines  at the bottom of this Scoreboard.
     *
     * @param text text to add
     * @return this Scoreboard
     */
    Scoreboard newLine(Collection<TextEntry> text);

    /**
     * Adds new line at the bottom of this Scoreboard.
     *
     * @param text the text
     * @return this Scoreboard
     */
    Scoreboard newLine(TextEntry text);

    /**
     * Sets new line. If the given line already exists, it's replaced.
     *
     * @param line where to add
     * @param text text to add
     * @return this Scoreboard
     */
    Scoreboard setLine(int line, TextEntry text);

    /**
     * Tries to set the line by {@link TextEntry}.
     * If the {@link TextEntry#getIdentifier()} is empty, creates new line.
     * If not, it tries to replace given text to the line that has this identifier.
     *
     * @param entry TextEntry
     * @return this Scoreboard
     */
    Scoreboard setLine(TextEntry entry);

    /**
     * Adds new line to the position and moves everything below.
     *
     * @param line where to add
     * @param text text to add
     * @return this Scoreboard
     */
    Scoreboard addLine(int line, TextEntry text);

    /**
     * Removes given line from the Scoreboard.
     *
     * @param line line to remove
     * @return this Scoreboard
     */
    Scoreboard removeLine(int line);

    /**
     * Removes given line from the Scoreboard.
     *
     * @param identifier a {@link TextEntry} identifier
     * @return this Scoreboard
     */
    Scoreboard removeLine(String identifier);

    /**
     * Replaces all lines with new ones.
     *
     * @param lines new lines
     * @return this Scoreboard
     */
    Scoreboard replaceLines(Map<Integer, TextEntry> lines);

    /**
     * Replaces lines with given collection.
     * The list is converted to TreeMap by it's given order
     *
     * @param lines new lines
     * @return this Scoreboard
     */
    Scoreboard replaceLines(List<TextEntry> lines);

    Collection<ScoreboardTeam> getTeams();

    Optional<ScoreboardTeam> getTeam(String identifier);



}
