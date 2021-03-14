package org.screamingsandals.lib.utils;

import java.util.Arrays;
import java.util.List;

public enum GameMode {
    CREATIVE,
    SURVIVAL,
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
