package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerChatEvent;

import java.util.stream.Collectors;

public class AsyncPlayerChatEventListener extends AbstractBukkitEventHandlerFactory<AsyncPlayerChatEvent, SPlayerChatEvent> {
    public AsyncPlayerChatEventListener(Plugin plugin) {
        super(AsyncPlayerChatEvent.class, SPlayerChatEvent.class, plugin);
    }

    @Override
    protected SPlayerChatEvent wrapEvent(AsyncPlayerChatEvent event, EventPriority priority) {
        return new SPlayerChatEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                event.getMessage(),
                event.getFormat(),
                event.getRecipients().stream().map(PlayerMapper::wrapPlayer).collect(Collectors.toList())
        );
    }

    @Override
    protected void postProcess(SPlayerChatEvent wrappedEvent, AsyncPlayerChatEvent event) {
        event.setCancelled(wrappedEvent.isCancelled());
        event.setMessage(wrappedEvent.getMessage());
        event.setFormat(wrappedEvent.getFormat());
        event.getRecipients().clear();
        event.getRecipients().addAll(wrappedEvent.getRecipients().stream().map(w -> w.as(Player.class)).collect(Collectors.toList()));
    }
}
