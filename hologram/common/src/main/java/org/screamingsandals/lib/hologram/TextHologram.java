package org.screamingsandals.lib.hologram;

import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.TreeMap;

/**
 * Hologram that shows some text.
 */
public interface TextHologram extends Hologram {

    /**
     * This is copy of the original data.
     * Changing this will have no effects on the actual hologram.
     *
     * @return a new {@link TreeMap} with lines.
     */
    TreeMap<Integer, Component> getLines();

    /**
     * Adds new line to the hologram and moves everything else 1 line down.
     *
     * @param text text to add
     * @return this hologram
     */
    TextHologram firstLine(Component text);

    /**
     * Adds given collection of new lines  at the bottom of this hologram.
     *
     * @param text text to add
     * @return this hologram
     */
    TextHologram newLine(List<Component> text);

    /**
     * Adds new line at the bottom of this hologram.
     *
     * @param text
     * @return this hologram
     */
    TextHologram newLine(Component text);

    /**
     * Adds new line
     *
     * @param line where to add
     * @param text text to add
     * @return this hologram
     */
    TextHologram newLine(int line, Component text);

    /**
     * Removes given line from the hologram.
     *
     * @param line line to remove
     * @return this hologram
     */
    TextHologram removeLine(int line);

    /**
     * Replaces all lines with new ones.
     *
     * @param lines new lines
     * @return this hologram
     */
    TextHologram replaceLines(TreeMap<Integer, Component> lines);
}
