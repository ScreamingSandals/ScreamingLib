package org.screamingsandals.lib.gamecore.core;

import java.util.Optional;

public enum GameType {
    SINGLE_GAME_BUNGEE,
    MULTI_GAME_BUNGEE,
    MULTI_GAME,
    CUSTOM;

    public static Optional<GameType> get(String name) {
        final var lowerCaseName = name.toLowerCase();
        for (GameType type : GameType.values()) {
            if (type.name().toLowerCase().equalsIgnoreCase(lowerCaseName)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
