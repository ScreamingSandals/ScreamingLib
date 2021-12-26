package org.screamingsandals.lib.sender;

import org.screamingsandals.lib.utils.Wrapper;

import java.util.Optional;
import java.util.UUID;

public interface MultiPlatformOfflinePlayer extends Operator, Wrapper {
    UUID getUuid();

    /**
     * <p>Gets the player's last name.</p>
     *
     * @return the player's last name
     */
    Optional<String> getLastName();

    /**
     * <p>Gets the first played time.</p>
     *
     * @return the first played time
     */
    long getFirstPlayed();

    /**
     * <p>Gets the last played time.</p>
     *
     * @return the last played time
     */
    long getLastPlayed();

    default boolean hasPlayed() {
        return getFirstPlayed() > 0;
    }

    /**
     * <p>Checks if this player is banned.</p>
     *
     * @return is the player banned?
     */
    boolean isBanned();

    /**
     * <p>Checks if this player is whitelisted.</p>
     *
     * @return is the player whitelisted?
     */
    boolean isWhitelisted();

    /**
     * <p>Checks if this player is online.</p>
     *
     * @return is the player online?
     */
    boolean isOnline();

    /**
     * <p>Sets the whitelisted status for this player.</p>
     *
     * @param whitelisted the new whitelisted status
     */
    void setWhitelisted(boolean whitelisted);
}
