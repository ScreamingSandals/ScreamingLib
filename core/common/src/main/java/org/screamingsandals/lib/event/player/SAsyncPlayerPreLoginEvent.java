package org.screamingsandals.lib.event.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.event.AbstractAsyncEvent;

import java.net.InetAddress;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
public class SAsyncPlayerPreLoginEvent extends AbstractAsyncEvent {
    private final UUID uuid;
    private final InetAddress address;
    //name is changeable only on some platforms!
    private String name;
    private Result result = Result.ALLOWED;
    private Component message;

    public SAsyncPlayerPreLoginEvent(UUID uuid, String name, InetAddress address) {
        this.uuid = uuid;
        this.name = name;
        this.address = address;
    }

    //from paper
    public enum Result {
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
