package org.screamingsandals.lib.utils;
import java.util.List;
import java.util.Optional;

/**
 * Represents the various difficulty levels that are available.
 */
public enum Difficulty {
    /**
     * Players regain health over time, hostile mobs don't spawn, the hunger
     * bar does not deplete.
     */
    PEACEFUL(0),

    /**
     * Hostile mobs spawn, enemies deal less damage than on normal difficulty,
     * the hunger bar does deplete and starving deals up to 5 hearts of
     * damage. (Default value)
     */
    EASY(1),

    /**
     * Hostile mobs spawn, enemies deal normal amounts of damage, the hunger
     * bar does deplete and starving deals up to 9.5 hearts of damage.
     */
    NORMAL(2),

    /**
     * Hostile mobs spawn, enemies deal greater damage than on normal
     * difficulty, the hunger bar does deplete and starving can kill players.
     */
    HARD(3);

    private final int value;

    Difficulty(final int value) {
        this.value = value;
    }

    /**
     * Gets the difficulty value associated with this Difficulty.
     *
     * @return An integer value of this difficulty
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the Difficulty represented by the specified value
     *
     * @param value Value to check
     * @return Associative {@link Difficulty} with the given value, or null if
     *     it doesn't exist
     */
    public static Optional<Difficulty> getByValue(final int value) {
        return List.of(values()).stream()
                .filter(difficulty -> difficulty.getValue() == value)
                .findAny();
    }
}
