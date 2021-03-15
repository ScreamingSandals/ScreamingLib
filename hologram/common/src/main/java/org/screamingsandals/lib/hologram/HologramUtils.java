package org.screamingsandals.lib.hologram;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;

import java.util.concurrent.ConcurrentSkipListMap;

@UtilityClass
public class HologramUtils {

    public ConcurrentSkipListMap<Integer, Component> addEntryAndMoveRest(ConcurrentSkipListMap<Integer, Component> input, Integer line, Component text) {
        if (!input.containsKey(line)) {
            input.put(line, text);
            return input;
        }

        final var head = input.headMap(line);
        final var tail = input.tailMap(line);

        final ConcurrentSkipListMap<Integer, Component> toReturn = new ConcurrentSkipListMap<>(head);
        tail.forEach((key, value) -> toReturn.put(key + 1, value));
        toReturn.put(line, text);
        return toReturn;
    }

    public ConcurrentSkipListMap<Integer, Component> removeEntryAndMoveRest(ConcurrentSkipListMap<Integer, Component> input, Integer line) {
        if (!input.containsKey(line)) {
            return input;
        }

        final var head = input.headMap(line);
        final var tail = input.tailMap(line);

        final ConcurrentSkipListMap<Integer, Component> toReturn = new ConcurrentSkipListMap<>(head);
        tail.forEach((key, value) -> toReturn.put(key - 1, value));
        return toReturn;
    }

}
