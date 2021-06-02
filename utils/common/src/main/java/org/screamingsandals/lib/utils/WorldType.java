package org.screamingsandals.lib.utils;


import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Optional;

public enum WorldType {
    NORMAL("DEFAULT"),
    FLAT("FLAT"),
    LARGE_BIOMES("LARGEBIOMES"),
    AMPLIFIED("AMPLIFIED");

    private final String name;

    WorldType(String name) {
        this.name = name;
    }

    /**
     * Gets the name of this WorldType
     *
     * @return Name of this type
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Gets a WorldType by its name
     *
     * @param name Name of the WorldType to get
     * @return Requested WorldType, or null if not found
     */
    public static Optional<WorldType> getByName(@NotNull String name) {
        return List.of(values()).stream()
                .filter(worldType -> worldType.getName().equals(name.toUpperCase()))
                .findAny();
    }
}