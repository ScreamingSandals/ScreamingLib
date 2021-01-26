package org.screamingsandals.lib.velocity.proxiedplayer.event;

import com.velocitypowered.api.proxy.ProxyServer;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.proxiedplayer.ProxiedPlayerMapper;
import org.screamingsandals.lib.proxiedplayer.event.PlayerChatEvent;
import org.screamingsandals.lib.velocity.event.AbstractEventHandlerFactory;

public class ChatEventHandlerFactory extends
        AbstractEventHandlerFactory<com.velocitypowered.api.event.player.PlayerChatEvent, PlayerChatEvent> {

    public ChatEventHandlerFactory(Object plugin, ProxyServer proxyServer) {
        super(com.velocitypowered.api.event.player.PlayerChatEvent.class, plugin, proxyServer);
    }

    @Override
    protected PlayerChatEvent wrapEvent(com.velocitypowered.api.event.player.PlayerChatEvent event, EventPriority priority) {
        return new PlayerChatEvent(ProxiedPlayerMapper.wrapPlayer(event.getPlayer()),
                event.getResult().getMessage().orElse(event.getMessage()), event.getResult().isAllowed(),
                event.getMessage().startsWith("/"));
    }

    @Override
    protected void handleResult(PlayerChatEvent wrappedEvent, com.velocitypowered.api.event.player.PlayerChatEvent event) {
        if (wrappedEvent.isCancelled()) {
            event.setResult(com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult.denied());
        } else {
            event.setResult(com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult.message(wrappedEvent.getMessage()));
        }
    }
}
