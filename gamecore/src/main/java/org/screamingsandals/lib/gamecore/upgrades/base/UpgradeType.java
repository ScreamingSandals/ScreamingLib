package org.screamingsandals.lib.gamecore.upgrades.base;

import java.util.Optional;

public enum UpgradeType {
    TEAM,
    STORE,
    SPAWNER,
    PLAYER,
    OTHER,
    CUSTOM;

    public static Optional<UpgradeType> get(String name) {
        final var lowerCaseName = name.toLowerCase();
        for (UpgradeType type : UpgradeType.values()) {
            if (type.name().toLowerCase().equalsIgnoreCase(lowerCaseName)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
