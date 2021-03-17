package org.screamingsandals.lib.visuals;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.utils.visual.TextEntry;

import java.util.Map;

/**
 * A visual that can have lines.
 */
public interface LinedVisual<T> extends Visual<T> {

    /**
     * This is immutable copy of the existing lines.
     * Changes to this will have no effects on the actual lines! :) (and shh, you will get exception probably)
     *
     * @return currently displayed lines.
     */
    Map<Integer, TextEntry> getLines();

    /**
     * Adds new line to this visual and moves everything else 1 line down.
     *
     * @param text text to add
     * @return this builder
     */
    T firstLine(Component text);

    /**
     * Adds new line to this visual and moves everything else 1 line down.
     *
     * @param text text to add
     * @return this builder
     */
    T firstLine(TextEntry text);

    /**
     * Adds new line to the bottom of this visual.
     *
     * @param text text to add
     * @return this builder
     */
    T bottomLine(Component text);

    /**
     * Adds new line to the bottom of this visual.
     *
     * @param text text to add
     * @return this builder
     */
    T bottomLine(TextEntry text);

    /**
     * Replaces a line that has {@link TextEntry#getIdentifier()} on it.
     *
     * @param text text to replace
     * @return this builder
     */
    T replaceLine(TextEntry text);

    /**
     * Replaces a line.
     *
     * @param where where to replace
     * @param text  text to replace
     * @return this builder
     */
    T replaceLine(Integer where, Component text);

    /**
     * Replaces a line.
     *
     * @param where where to replace
     * @param text  text to replace
     * @return this builder
     */
    T replaceLine(Integer where, TextEntry text);

    /**
     * Replaces all lines.
     *
     * @param lines new lines :)
     * @return this builder
     */
    T replaceLine(Map<Integer, TextEntry> lines);

    /**
     * Creates a new line and moves everything bellow if anything is already on that line.
     *
     * @param where where to add
     * @param text  text to add
     * @return this builder
     */
    T newLine(Integer where, Component text);

    /**
     * Creates a new line and moves everything bellow if anything is already on that line.
     *
     * @param where where to add
     * @param text  text to add
     * @return this builder
     */
    T newLine(Integer where, TextEntry text);

    /**
     * Removes line on given location.
     *
     * @param where where to remove
     * @return this builder
     */
    T removeLine(Integer where);

    /**
     * Removes line by given identifier.
     *
     * @param identifier identifier to remove
     * @return this builder
     */
    T removeLine(String identifier);
}
