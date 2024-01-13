/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.visuals.utils;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.visual.TextEntry;

import java.util.concurrent.ConcurrentSkipListMap;

@UtilityClass
public class VisualUtils {

    public @NotNull ConcurrentSkipListMap<@NotNull Integer, TextEntry> addEntryAndMoveRest(@NotNull ConcurrentSkipListMap<@NotNull Integer, TextEntry> input, @NotNull Integer line, @NotNull TextEntry text) {
        if (!input.containsKey(line)) {
            input.put(line, text);
            return input;
        }

        final var head = input.headMap(line);
        final var tail = input.tailMap(line);

        final ConcurrentSkipListMap<Integer, TextEntry> toReturn = new ConcurrentSkipListMap<>(head);
        tail.forEach((key, value) -> toReturn.put(key + 1, value));
        toReturn.put(line, text);
        return toReturn;
    }

    public @NotNull ConcurrentSkipListMap<@NotNull Integer, TextEntry> removeEntryAndMoveRest(@NotNull ConcurrentSkipListMap<@NotNull Integer, TextEntry> input, @NotNull Integer line) {
        if (!input.containsKey(line)) {
            return input;
        }

        final var head = input.headMap(line);
        final var tail = input.tailMap(line);

        final ConcurrentSkipListMap<Integer, TextEntry> toReturn = new ConcurrentSkipListMap<>(head);
        tail.forEach((key, value) -> toReturn.put(key - 1, value));
        return toReturn;
    }

}
