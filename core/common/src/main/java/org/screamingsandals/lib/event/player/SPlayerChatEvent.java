package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
public class SPlayerChatEvent extends SPlayerCancellableEvent {
    private final ObjectLink<String> message;
    private final ObjectLink<String> format;
    private final Collection<PlayerWrapper> recipients;

    public SPlayerChatEvent(ImmutableObjectLink<PlayerWrapper> player,
                            ObjectLink<String> message,
                            ObjectLink<String> format,
                            Collection<PlayerWrapper> recipients) {
        super(player);
        this.message = message;
        this.format = format;
        this.recipients = recipients;
    }

    public PlayerWrapper getSender() {
        return getPlayer();
    }

    public String getMessage() {
        return message.get();
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public String getFormat() {
        return format.get();
    }

    public void setFormat(String format) {
        this.format.set(format);
    }
}
