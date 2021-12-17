package org.screamingsandals.lib.event.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SAsyncEvent;

import java.net.InetAddress;
import java.util.UUID;

public interface SAsyncPlayerPreLoginEvent extends SAsyncEvent, PlatformEventWrapper {

    UUID getUuid();

    InetAddress getAddress();

    String getName();

    default void setName(String name) {
        throw new UnsupportedOperationException("Name is not changeable on this platform!");
    }

    Result getResult();

    void setResult(Result result);

    Component getMessage();

    void setMessage(Component message);

    void setMessage(ComponentLike message);

    //from paper
    enum Result {
        /**
         * The player is allowed to log in
         */
        ALLOWED,
        /**
         * The player is not allowed to log in, due to the server being full
         */
        KICK_FULL,
        /**
         * The player is not allowed to log in, due to them being banned
         */
        KICK_BANNED,
        /**
         * The player is not allowed to log in, due to them not being on the
         * white list
         */
        KICK_WHITELIST,
        /**
         * The player is not allowed to log in, for reasons undefined
         */
        KICK_OTHER;
    }
}
