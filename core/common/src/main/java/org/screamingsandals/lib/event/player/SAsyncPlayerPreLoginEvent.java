package org.screamingsandals.lib.event.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.screamingsandals.lib.event.AbstractAsyncEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

import java.net.InetAddress;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
public class SAsyncPlayerPreLoginEvent extends AbstractAsyncEvent implements SPlayerEvent {
    private final ImmutableObjectLink<UUID> uuid;
    private final ImmutableObjectLink<InetAddress> address;
    //name is changeable only on some platforms!
    private final ObjectLink<String> name;
    private final ObjectLink<Result> result;
    private final ObjectLink<Component> message;

    public UUID getUuid() {
        return uuid.get();
    }

    public InetAddress getAddress() {
        return address.get();
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Result getResult() {
        return result.get();
    }

    public void setResult(Result result) {
        this.result.set(result);
    }

    public Component getMessage() {
        return message.get();
    }

    public void setMessage(Component message) {
        this.message.set(message);
    }

    public void setMessage(ComponentLike message) {
        this.message.set(message.asComponent());
    }

    @Override
    public PlayerWrapper getPlayer() {
        throw new UnsupportedOperationException("Cannot call getPlayer() on " + this.getClass().getSimpleName() + " as player instance is not constructed yet!");
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
