package org.screamingsandals.lib.utils;

import java.util.Arrays;
import java.util.List;

// TODO: Replace with mapping in core module
public enum GameMode {
    SURVIVAL,
    CREATIVE,
    ADVENTURE,
    SPECTATOR;

    public static final List<GameMode> VALUES = Arrays.asList(values());

    public static GameMode convert(String input) {
        return VALUES.stream()
                .filter(next -> input.equalsIgnoreCase(next.name()))
                .findFirst()
                .orElse(GameMode.SURVIVAL);
    }
}
