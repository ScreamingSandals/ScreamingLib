package org.screamingsandals.lib.event.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.net.InetAddress;

public interface SPlayerLoginEvent extends SPlayerEvent, PlatformEventWrapper {

    PlayerWrapper getPlayer();

    InetAddress getAddress();

    String getHostname();

    SAsyncPlayerPreLoginEvent.Result getResult();

    void setResult(SAsyncPlayerPreLoginEvent.Result result);

    Component getMessage();

    void setMessage(Component message);

    void setMessage(ComponentLike message);

    /**
     * Allows the player to log in
     */
    default void allow() {
        setResult(SAsyncPlayerPreLoginEvent.Result.ALLOWED);
        setMessage(Component.empty());
    }

    /**
     * Disallows the player from logging in, with the given reason
     *
     * @param result New result for disallowing the player
     * @param message Kick message to display to the user
     */
    default void disallow(@NotNull final SAsyncPlayerPreLoginEvent.Result result, @NotNull final Component message) {
        setResult(result);
        setMessage(message);
    }
}
