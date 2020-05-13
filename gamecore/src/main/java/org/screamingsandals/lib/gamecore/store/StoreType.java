package org.screamingsandals.lib.gamecore.store;

import java.util.Optional;

public enum StoreType {
    GENERIC,
    UPGRADES,
    CUSTOM;

    public static Optional<StoreType> get(String name) {
        final var upperCaseName = name.toUpperCase();
        for (StoreType type : StoreType.values()) {
            if (type.name().equalsIgnoreCase(upperCaseName)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
