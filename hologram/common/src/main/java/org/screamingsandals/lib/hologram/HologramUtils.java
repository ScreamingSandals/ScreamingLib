package org.screamingsandals.lib.hologram;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;

import java.util.TreeMap;

@UtilityClass
public class HologramUtils {

    public TreeMap<Integer, Component> addEntryAndMoveRest(TreeMap<Integer, Component> input, Integer line, Component text) {
        if (!input.containsKey(line)) {
            input.put(line, text);
            return input;
        }

        final var head = input.headMap(line);
        final var tail = input.tailMap(line);

        final TreeMap<Integer, Component> toReturn = new TreeMap<>(head);
        tail.forEach((key, value) -> toReturn.put(key + 1, value));
        return toReturn;
    }
}
