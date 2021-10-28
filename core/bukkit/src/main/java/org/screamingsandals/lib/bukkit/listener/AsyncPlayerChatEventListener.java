package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.player.SPlayerChatEvent;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class AsyncPlayerChatEventListener extends AbstractBukkitEventHandlerFactory<AsyncPlayerChatEvent, SPlayerChatEvent> {
    public AsyncPlayerChatEventListener(Plugin plugin) {
        super(AsyncPlayerChatEvent.class, SPlayerChatEvent.class, plugin);
    }

    @Override
    protected SPlayerChatEvent wrapEvent(AsyncPlayerChatEvent event, EventPriority priority) {
        return new SPlayerChatEvent(
                ImmutableObjectLink.of(() -> new BukkitEntityPlayer(event.getPlayer())),
                ObjectLink.of(event::getMessage, event::setMessage),
                ObjectLink.of(event::getFormat, event::setFormat),
                new CollectionLinkedToCollection<>(event.getRecipients(), playerWrapper -> playerWrapper.as(Player.class), BukkitEntityPlayer::new)
        );
    }
}
