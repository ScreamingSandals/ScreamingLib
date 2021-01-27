package org.screamingsandals.lib.velocity.proxiedplayer.event;

import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.proxiedplayer.ProxiedPlayerMapper;
import org.screamingsandals.lib.proxiedplayer.event.SPlayerChatEvent;
import org.screamingsandals.lib.velocity.event.AbstractEventHandlerFactory;

public class ChatEventHandlerFactory extends
        AbstractEventHandlerFactory<PlayerChatEvent, SPlayerChatEvent> {

    public ChatEventHandlerFactory(Object plugin, ProxyServer proxyServer) {
        super(PlayerChatEvent.class, plugin, proxyServer);
    }

    @Override
    protected SPlayerChatEvent wrapEvent(PlayerChatEvent event, EventPriority priority) {
        return new SPlayerChatEvent(ProxiedPlayerMapper.wrapPlayer(event.getPlayer()), event.getMessage().startsWith("/"),
                event.getResult().getMessage().orElse(event.getMessage()), event.getResult().isAllowed());
    }

    @Override
    protected void handleResult(SPlayerChatEvent wrappedEvent, PlayerChatEvent event) {
        if (wrappedEvent.isCancelled()) {
            event.setResult(PlayerChatEvent.ChatResult.denied());
        } else {
            event.setResult(PlayerChatEvent.ChatResult.message(wrappedEvent.getMessage()));
        }
    }
}
