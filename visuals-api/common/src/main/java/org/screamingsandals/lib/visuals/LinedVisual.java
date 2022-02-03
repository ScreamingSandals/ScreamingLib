/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.lib.visuals;

import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.utils.visual.TextEntry;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
    Map<Integer, TextEntry> lines();

    /**
     * Tries to get a line by given identifier.
     * @param identifier where's my identity?!
     * @return this visual
     */
    Optional<Map.Entry<Integer, TextEntry>> lineByIdentifier(String identifier);

    /**
     * Adds new line to this visual and moves everything else 1 line down.
     *
     * @param text text to add
     * @return this visual
     */
    T firstLine(Component text);

    /**
     * Adds new line to this visual and moves everything else 1 line down.
     *
     * @param text text to add
     * @return this visual
     */
    T firstLine(ComponentLike text);

    /**
     * Adds new line to this visual and moves everything else 1 line down.
     *
     * @param text text to add
     * @return this visual
     */
    T firstLine(TextEntry text);

    /**
     * Adds new line to the bottom of this visual.
     *
     * @param text text to add
     * @return this visual
     */
    T bottomLine(Component text);

    /**
     * Adds new line to the bottom of this visual.
     *
     * @param text text to add
     * @return this visual
     */
    T bottomLine(ComponentLike text);

    /**
     * Adds new line to the bottom of this visual.
     *
     * @param text text to add
     * @return this visual
     */
    T bottomLine(TextEntry text);

    /**
     * Replaces a line that has {@link TextEntry#getIdentifier()} on it.
     *
     * @param text text to replace
     * @return this visual
     */
    T replaceLine(TextEntry text);

    /**
     * Replaces a line.
     *
     * @param where where to replace
     * @param text  text to replace
     * @return this visual
     */
    T replaceLine(Integer where, Component text);

    /**
     * Replaces a line.
     *
     * @param where where to replace
     * @param text  text to replace
     * @return this visual
     */
    T replaceLine(Integer where, ComponentLike text);

    /**
     * Replaces a line.
     *
     * @param where where to replace
     * @param text  text to replace
     * @return this visual
     */
    T replaceLine(Integer where, TextEntry text);

    /**
     * Replaces all lines.
     *
     * @param lines new lines :)
     * @return this visual
     */
    T setLines(Map<Integer, TextEntry> lines);

    /**
     * Replaces all lines.
     * @param lines new lines :)
     * @return this visual
     */
    T setLines(List<Component> lines);

    /**
     * Replaces all lines.
     * @param lines new lines :)
     * @return this visual
     */
    T setLines(Set<TextEntry> lines);

    /**
     * Creates a new line and moves everything bellow if anything is already on that line.
     *
     * @param where where to add
     * @param text  text to add
     * @return this visual
     */
    T newLine(Integer where, Component text);

    /**
     * Creates a new line and moves everything bellow if anything is already on that line.
     *
     * @param where where to add
     * @param text  text to add
     * @return this visual
     */
    T newLine(Integer where, ComponentLike text);

    /**
     * Creates a new line and moves everything bellow if anything is already on that line.
     *
     * @param where where to add
     * @param text  text to add
     * @return this visual
     */
    T newLine(Integer where, TextEntry text);

    /**
     * Removes line on given location.
     *
     * @param where where to remove
     * @return this visual
     */
    T removeLine(Integer where);

    /**
     * Removes line by given identifier.
     *
     * @param identifier identifier to remove
     * @return this visual
     */
    T removeLine(String identifier);
}
