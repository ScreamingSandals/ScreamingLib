package org.screamingsandals.lib.gamecore.core.cycle;

import java.util.Optional;

public enum CycleType {
    SINGLE_GAME_BUNGEE,
    MULTI_GAME_BUNGEE,
    MULTI_GAME,
    CUSTOM;

    public static Optional<CycleType> get(String name) {
        final var lowerCaseName = name.toLowerCase();
        for (CycleType type : CycleType.values()) {
            if (type.name().toLowerCase().equalsIgnoreCase(lowerCaseName)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
