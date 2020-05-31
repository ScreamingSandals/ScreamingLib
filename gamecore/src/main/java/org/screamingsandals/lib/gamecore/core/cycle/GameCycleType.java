package org.screamingsandals.lib.gamecore.core.cycle;

import java.util.Optional;

public enum GameCycleType {
    SINGLE_GAME_BUNGEE,
    MULTI_GAME_BUNGEE,
    MULTI_GAME,
    CUSTOM;

    public static Optional<GameCycleType> get(String name) {
        final var lowerCaseName = name.toLowerCase();
        for (GameCycleType type : GameCycleType.values()) {
            if (type.name().toLowerCase().equalsIgnoreCase(lowerCaseName)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
