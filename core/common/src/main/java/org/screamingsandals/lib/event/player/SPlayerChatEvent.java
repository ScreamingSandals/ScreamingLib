package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.Collection;

public interface SPlayerChatEvent extends SCancellableEvent, SPlayerEvent, PlatformEventWrapper {
    Collection<PlayerWrapper> getRecipients();

    PlayerWrapper getSender();

    String getMessage();

    void setMessage(String message);

    String getFormat();

    void setFormat(String format);

    /**
     * same as {@link SPlayerChatEvent#getSender()}
     * @return
     */
    @Override
    default PlayerWrapper getPlayer() {
        return getSender();
    }
}
