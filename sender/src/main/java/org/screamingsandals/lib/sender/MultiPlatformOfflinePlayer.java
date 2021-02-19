package org.screamingsandals.lib.sender;

import org.screamingsandals.lib.utils.Wrapper;

import java.util.Optional;
import java.util.UUID;

public interface MultiPlatformOfflinePlayer extends Operator, Wrapper {
    UUID getUuid();

    Optional<String> getLastName();

    long getFirstPlayed();

    long getLastPlayed();

    default boolean hasPlayed() {
        return getFirstPlayed() > 0;
    }

    boolean isBanned();

    boolean isWhitelisted();

    boolean isOnline();

    void setWhitelisted(boolean whitelisted);
}
