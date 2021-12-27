package org.screamingsandals.lib.sender;

import org.screamingsandals.lib.utils.Wrapper;

import java.util.Optional;
import java.util.UUID;

public interface MultiPlatformOfflinePlayer extends Operator, Wrapper {
    UUID getUuid();

    /**
     * Gets the player's last name.
     *
     * @return the player's last name
     */
    Optional<String> getLastName();

    /**
     * Gets the first played time.
     *
     * @return the first played time
     */
    long getFirstPlayed();

    /**
     * Gets the last played time.
     *
     * @return the last played time
     */
    long getLastPlayed();

    default boolean hasPlayed() {
        return getFirstPlayed() > 0;
    }

    /**
     * Checks if this player is banned.
     *
     * @return is the player banned?
     */
    boolean isBanned();

    /**
     * Checks if this player is whitelisted.
     *
     * @return is the player whitelisted?
     */
    boolean isWhitelisted();

    /**
     * Checks if this player is online.
     *
     * @return is the player online?
     */
    boolean isOnline();

    /**
     * Sets the whitelisted status for this player.
     *
     * @param whitelisted the new whitelisted status
     */
    void setWhitelisted(boolean whitelisted);
}
