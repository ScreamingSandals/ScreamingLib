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

package org.screamingsandals.lib.visuals.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.screamingsandals.lib.utils.visual.TextEntry;
import org.screamingsandals.lib.visuals.LinedVisual;
import org.screamingsandals.lib.visuals.Visual;
import org.screamingsandals.lib.visuals.utils.VisualUtils;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public abstract class AbstractLinedVisual<T extends Visual<T>> extends AbstractVisual<T> implements LinedVisual<T> {
    protected ConcurrentSkipListMap<Integer, TextEntry> lines = new ConcurrentSkipListMap<>();
    protected Integer originalLinesSize = 0;

    public AbstractLinedVisual(UUID uuid) {
        super(uuid);
    }

    @Override
    public Map<Integer, TextEntry> lines() {
        return Map.copyOf(lines);
    }

    @Override
    public Optional<Map.Entry<Integer, TextEntry>> lineByIdentifier(String identifier) {
        return lines.entrySet()
                .stream()
                .filter(next -> next.getValue().getIdentifier().equals(identifier))
                .map(next -> Map.entry(next.getKey(), next.getValue()))
                .findFirst();
    }

    @Override
    public T title(Component title) {
        return firstLine(title);
    }

    @Override
    public T title(ComponentLike title) {
        return firstLine(title);
    }

    @Override
    public T firstLine(Component text) {
        return newLine(0, text);
    }

    @Override
    public T firstLine(ComponentLike text) {
        return newLine(0, text);
    }

    @Override
    public T firstLine(TextEntry text) {
        return newLine(0, text);
    }

    @Override
    public T bottomLine(Component text) {
        return bottomLine(TextEntry.of(text));
    }

    @Override
    public T bottomLine(ComponentLike text) {
        return bottomLine(TextEntry.of(text));
    }

    @SuppressWarnings("unchecked")
    @Override
    public T bottomLine(TextEntry text) {
        if (lines.isEmpty()) {
            return firstLine(text);
        }

        originalLinesSize = lines.size();
        lines.put(lines.lastKey() + 1, text);
        update();
        return (T) this;
    }

    @Override
    public T replaceLine(TextEntry text) {
        final var identifier = text.getIdentifier();
        if (identifier.isEmpty()) {
            return bottomLine(text);
        }

        final var line = lineByIdentifier(text.getIdentifier());
        if (line.isEmpty()) {
            return bottomLine(text);
        }
        return replaceLine(line.get().getKey(), text);
    }

    @Override
    public T replaceLine(Integer where, Component text) {
        return replaceLine(where, TextEntry.of(text));
    }

    @Override
    public T replaceLine(Integer where, ComponentLike text) {
        return replaceLine(where, TextEntry.of(text));
    }

    @SuppressWarnings("unchecked")
    @Override
    public T replaceLine(Integer where, TextEntry text) {
        if (!lines.containsKey(where)) {
            return newLine(where, text);
        }
        originalLinesSize = lines.size();
        lines.put(where, text);
        update();
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T setLines(Map<Integer, TextEntry> lines) {
        originalLinesSize = this.lines.size();
        this.lines = new ConcurrentSkipListMap<>(lines);
        update();
        return (T) this;
    }

    @Override
    public T setLines(List<Component> lines) {
        final var toSet = new HashMap<Integer, TextEntry>();
        for (int i = 0; i < lines.size(); i++) {
            toSet.put(i, TextEntry.of(lines.get(i)));
        }
        return setLines(toSet);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T setLines(Set<TextEntry> lines) {
        final var ls = List.copyOf(lines);
        final var toSet = new HashMap<Integer, TextEntry>();
        for (int i = 0; i < ls.size(); i++) {
            toSet.put(i, ls.get(i));
        }
        return setLines(toSet);
    }

    @Override
    public T newLine(Integer where, Component text) {
        return newLine(where, TextEntry.of(text));
    }

    @Override
    public T newLine(Integer where, ComponentLike text) {
        return newLine(where, TextEntry.of(text));
    }

    @SuppressWarnings("unchecked")
    @Override
    public T newLine(Integer where, TextEntry text) {
        originalLinesSize = lines.size();
        lines = VisualUtils.addEntryAndMoveRest(lines, where, text);
        update();
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T removeLine(Integer where) {
        originalLinesSize = lines.size();
        lines = VisualUtils.removeEntryAndMoveRest(lines, where);
        update();
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T removeLine(String identifier) {
        lineByIdentifier(identifier)
                .ifPresent(next -> {
                    originalLinesSize = lines.size();
                    lines = VisualUtils.removeEntryAndMoveRest(lines, next.getKey());
                    update();
                });
        return (T) this;
    }
}
