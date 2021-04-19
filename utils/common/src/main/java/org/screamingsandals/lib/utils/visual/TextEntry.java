package org.screamingsandals.lib.utils.visual;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.screamingsandals.lib.utils.AdventureHelper;

/**
 * TextEntry is used to identify certain text if not simply possible.
 */
public interface TextEntry {

    /**
     * New text entry!
     *
     * @param identifier identifier of this text entry.
     * @param text       actual text
     * @return baked text entry
     */
    static TextEntry of(String identifier, Component text) {
        return SimpleTextEntry.of(identifier, text);
    }

    /**
     * New text entry!
     *
     * @param text actual text
     * @return baked text entry with no identifier
     */
    static TextEntry of(Component text) {
        return SimpleTextEntry.of(text);
    }

    /**
     * New text entry!
     *
     * @param identifier identifier of this text entry.
     * @param text       actual text
     * @return baked text entry
     */
    static TextEntry of(String identifier, ComponentLike text) {
        return SimpleCLTextEntry.of(identifier, text);
    }

    /**
     * New text entry!
     *
     * @param text actual text
     * @return baked text entry with no identifier
     */
    static TextEntry of(ComponentLike text) {
        return SimpleCLTextEntry.of(text);
    }

    /**
     * Legacy support.
     *
     * @param identifier identifier of this text entry.
     * @param text       actual text
     * @return baked text entry
     */
    static TextEntry of(String identifier, String text) {
        return SimpleTextEntry.of(identifier, AdventureHelper.toComponent(text));
    }

    /**
     * Legacy support.
     *
     * @param text actual text
     * @return baked text entry with no identifier
     */
    static TextEntry of(String text) {
        return SimpleTextEntry.of(AdventureHelper.toComponent(text));
    }

    /**
     * Identifier of this entry.
     * This can be used to identify certain messages inside visuals or so.
     *
     * @return identifier or empty String if undefined.
     */
    String getIdentifier();

    /**
     * Text of this entry
     *
     * @return the actual text
     */
    Component getText();

    /**
     * Checks if the TextEntry is the same.
     *
     * @param another another entry
     * @return true if the identifier is the same.
     */
    default boolean isSame(TextEntry another) {
        return getIdentifier().equalsIgnoreCase(another.getIdentifier());
    }
}
