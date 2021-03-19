package org.screamingsandals.lib.visuals.utils;

import lombok.experimental.UtilityClass;
import org.screamingsandals.lib.utils.visual.TextEntry;

import java.util.concurrent.ConcurrentSkipListMap;

@UtilityClass
public class VisualUtils {

    public ConcurrentSkipListMap<Integer, TextEntry> addEntryAndMoveRest(ConcurrentSkipListMap<Integer, TextEntry> input, Integer line, TextEntry text) {
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

    public ConcurrentSkipListMap<Integer, TextEntry> removeEntryAndMoveRest(ConcurrentSkipListMap<Integer, TextEntry> input, Integer line) {
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
