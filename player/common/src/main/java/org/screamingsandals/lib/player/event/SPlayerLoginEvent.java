package org.screamingsandals.lib.player.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import java.net.InetAddress;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerLoginEvent extends AbstractEvent {
    private final PlayerWrapper player;
    private final InetAddress address;
    private final String hostname;
    private SAsyncPlayerPreLoginEvent.Result result;
    private Component message;

    /**
     * Allows the player to log in
     */
    public void allow() {
        result = SAsyncPlayerPreLoginEvent.Result.ALLOWED;
        message = Component.text("");
    }

    /**
     * Disallows the player from logging in, with the given reason
     *
     * @param result New result for disallowing the player
     * @param message Kick message to display to the user
     */
    public void disallow(@NotNull final SAsyncPlayerPreLoginEvent.Result result, @NotNull final Component message) {
        this.result = result;
        this.message = message;
    }
}
