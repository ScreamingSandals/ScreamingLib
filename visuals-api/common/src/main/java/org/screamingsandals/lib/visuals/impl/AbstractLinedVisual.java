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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.utils.visual.TextEntry;
import org.screamingsandals.lib.visuals.LinedVisual;
import org.screamingsandals.lib.visuals.Visual;
import org.screamingsandals.lib.visuals.utils.VisualUtils;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public abstract class AbstractLinedVisual<T extends Visual<T>> extends AbstractVisual<T> implements LinedVisual<T> {
    protected @NotNull ConcurrentSkipListMap<@NotNull Integer, @NotNull TextEntry> lines = new ConcurrentSkipListMap<>();
    protected Integer originalLinesSize = 0;

    public AbstractLinedVisual(UUID uuid) {
        super(uuid);
    }

    @Override
    public @NotNull Map<@NotNull Integer, @NotNull TextEntry> lines() {
        return Map.copyOf(lines);
    }

    @Override
    public @Nullable Map.Entry<Integer, TextEntry> lineByIdentifier(@NotNull String identifier) {
        return lines.entrySet()
                .stream()
                .filter(next -> next.getValue().getIdentifier().equals(identifier))
                .map(next -> Map.entry(next.getKey(), next.getValue()))
                .findFirst()
                .orElse(null);
    }

    @Contract("_ -> this")
    @Override
    public @NotNull T title(@NotNull Component title) {
        return firstLine(title);
    }

    @Contract("_ -> this")
    @Override
    public @NotNull T title(@NotNull ComponentLike title) {
        return firstLine(title);
    }

    @Contract("_ -> this")
    @Override
    public @NotNull T firstLine(@NotNull Component text) {
        return newLine(0, text);
    }

    @Contract("_ -> this")
    @Override
    public @NotNull T firstLine(@NotNull ComponentLike text) {
        return newLine(0, text);
    }

    @Contract("_ -> this")
    @Override
    public @NotNull T firstLine(@NotNull TextEntry text) {
        return newLine(0, text);
    }

    @Contract("_ -> this")
    @Override
    public @NotNull T bottomLine(@NotNull Component text) {
        return bottomLine(TextEntry.of(text));
    }

    @Contract("_ -> this")
    @Override
    public @NotNull T bottomLine(@NotNull ComponentLike text) {
        return bottomLine(TextEntry.of(text));
    }

    @Contract("_ -> this")
    @SuppressWarnings("unchecked")
    @Override
    public @NotNull T bottomLine(@NotNull TextEntry text) {
        if (lines.isEmpty()) {
            return firstLine(text);
        }

        originalLinesSize = lines.size();
        lines.put(lines.lastKey() + 1, text);
        update();
        return (T) this;
    }

    @Contract("_ -> this")
    @Override
    public @NotNull T replaceLine(@NotNull TextEntry text) {
        final var identifier = text.getIdentifier();
        if (identifier.isEmpty()) {
            return bottomLine(text);
        }

        final var line = lineByIdentifier(text.getIdentifier());
        if (line == null) {
            return bottomLine(text);
        }
        return replaceLine(line.getKey(), text);
    }

    @Contract("_, _ -> this")
    @Override
    public @NotNull T replaceLine(@NotNull Integer where, @NotNull Component text) {
        return replaceLine(where, TextEntry.of(text));
    }

    @Contract("_, _ -> this")
    @Override
    public @NotNull T replaceLine(@NotNull Integer where, @NotNull ComponentLike text) {
        return replaceLine(where, TextEntry.of(text));
    }

    @Contract("_, _ -> this")
    @SuppressWarnings("unchecked")
    @Override
    public @NotNull T replaceLine(@NotNull Integer where, @NotNull TextEntry text) {
        if (!lines.containsKey(where)) {
            return newLine(where, text);
        }
        originalLinesSize = lines.size();
        lines.put(where, text);
        update();
        return (T) this;
    }

    @Contract("_ -> this")
    @SuppressWarnings("unchecked")
    @Override
    public @NotNull T setLines(@NotNull Map<@NotNull Integer, @NotNull TextEntry> lines) {
        originalLinesSize = this.lines.size();
        this.lines = new ConcurrentSkipListMap<>(lines);
        update();
        return (T) this;
    }

    @Contract("_ -> this")
    @Override
    public @NotNull T setLines(@NotNull List<@NotNull Component> lines) {
        final var toSet = new HashMap<Integer, TextEntry>();
        for (int i = 0; i < lines.size(); i++) {
            toSet.put(i, TextEntry.of(lines.get(i)));
        }
        return setLines(toSet);
    }

    @Contract("_ -> this")
    @SuppressWarnings("unchecked")
    @Override
    public @NotNull T setLines(@NotNull Set<@NotNull TextEntry> lines) {
        final var ls = List.copyOf(lines);
        final var toSet = new HashMap<Integer, TextEntry>();
        for (int i = 0; i < ls.size(); i++) {
            toSet.put(i, ls.get(i));
        }
        return setLines(toSet);
    }

    @Contract("_, _ -> this")
    @Override
    public @NotNull T newLine(@NotNull Integer where, @NotNull Component text) {
        return newLine(where, TextEntry.of(text));
    }

    @Contract("_, _ -> this")
    @Override
    public @NotNull T newLine(@NotNull Integer where, @NotNull ComponentLike text) {
        return newLine(where, TextEntry.of(text));
    }

    @Contract("_, _ -> this")
    @SuppressWarnings("unchecked")
    @Override
    public @NotNull T newLine(@NotNull Integer where, @NotNull TextEntry text) {
        originalLinesSize = lines.size();
        lines = VisualUtils.addEntryAndMoveRest(lines, where, text);
        update();
        return (T) this;
    }

    @Contract("_ -> this")
    @SuppressWarnings("unchecked")
    @Override
    public @NotNull T removeLine(@NotNull Integer where) {
        originalLinesSize = lines.size();
        lines = VisualUtils.removeEntryAndMoveRest(lines, where);
        update();
        return (T) this;
    }

    @Contract("_ -> this")
    @SuppressWarnings("unchecked")
    @Override
    public @NotNull T removeLine(@NotNull String identifier) {
        var next = lineByIdentifier(identifier);
        if (next != null) {
            originalLinesSize = lines.size();
            lines = VisualUtils.removeEntryAndMoveRest(lines, next.getKey());
            update();
        }
        return (T) this;
    }
}
