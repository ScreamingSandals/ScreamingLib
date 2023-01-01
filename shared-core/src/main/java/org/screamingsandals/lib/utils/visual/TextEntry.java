/*
 * Copyright 2023 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.utils.visual;

import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;

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
        return SimpleTextEntry.of(identifier, Component.fromLegacy(text));
    }

    /**
     * Legacy support.
     *
     * @param text actual text
     * @return baked text entry with no identifier
     */
    static TextEntry of(String text) {
        return SimpleTextEntry.of(Component.fromLegacy(text));
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
