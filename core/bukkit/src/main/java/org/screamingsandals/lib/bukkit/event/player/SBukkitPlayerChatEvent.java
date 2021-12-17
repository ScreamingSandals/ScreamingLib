package org.screamingsandals.lib.bukkit.event.player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.player.SPlayerChatEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;

import java.util.Collection;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerChatEvent implements SPlayerChatEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final AsyncPlayerChatEvent event;

    // Internal cache
    private Collection<PlayerWrapper> recipients;
    private PlayerWrapper sender;

    @Override
    public Collection<PlayerWrapper> getRecipients() {
        if (recipients == null) {
            recipients = new CollectionLinkedToCollection<>(event.getRecipients(), playerWrapper -> playerWrapper.as(Player.class), BukkitEntityPlayer::new);
        }
        return recipients;
    }

    @Override
    public PlayerWrapper getSender() {
        if (sender == null) {
            sender = new BukkitEntityPlayer(event.getPlayer());
        }
        return sender;
    }

    @Override
    public String getMessage() {
        return event.getMessage();
    }

    @Override
    public void setMessage(String message) {
        event.setMessage(message);
    }

    @Override
    public String getFormat() {
        return event.getFormat();
    }

    @Override
    public void setFormat(String format) {
        event.setFormat(format);
    }
}
