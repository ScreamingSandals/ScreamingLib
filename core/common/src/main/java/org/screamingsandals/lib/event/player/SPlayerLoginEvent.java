package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

import java.net.InetAddress;

@EqualsAndHashCode(callSuper = false)
public class SPlayerLoginEvent extends SPlayerEvent {
    private final ImmutableObjectLink<InetAddress> address;
    private final ImmutableObjectLink<String> hostname;
    private final ObjectLink<SAsyncPlayerPreLoginEvent.Result> result;
    private final ObjectLink<Component> message;

    public SPlayerLoginEvent(ImmutableObjectLink<PlayerWrapper> player,
                             ImmutableObjectLink<InetAddress> address,
                             ImmutableObjectLink<String> hostname,
                             ObjectLink<SAsyncPlayerPreLoginEvent.Result> result,
                             ObjectLink<Component> message) {
        super(player);
        this.address = address;
        this.hostname = hostname;
        this.result = result;
        this.message = message;
    }

    public InetAddress getAddress() {
        return address.get();
    }

    public String getHostname() {
        return hostname.get();
    }

    public SAsyncPlayerPreLoginEvent.Result getResult() {
        return result.get();
    }

    public void setResult(SAsyncPlayerPreLoginEvent.Result result) {
        this.result.set(result);
    }

    public Component getMessage() {
        return message.get();
    }

    public void setMessage(Component message) {
        this.message.set(message);
    }

    /**
     * Allows the player to log in
     */
    public void allow() {
        setResult(SAsyncPlayerPreLoginEvent.Result.ALLOWED);
        setMessage(Component.empty());
    }

    /**
     * Disallows the player from logging in, with the given reason
     *
     * @param result New result for disallowing the player
     * @param message Kick message to display to the user
     */
    public void disallow(@NotNull final SAsyncPlayerPreLoginEvent.Result result, @NotNull final Component message) {
        setResult(result);
        setMessage(message);
    }
}
