package org.screamingsandals.lib.velocity.proxy.event;

import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.proxy.ProxiedPlayerMapper;
import org.screamingsandals.lib.proxy.event.SPlayerChatEvent;
import org.screamingsandals.lib.velocity.event.AbstractVelocityEventHandlerFactory;

public class ChatEventHandlerFactory extends
        AbstractVelocityEventHandlerFactory<PlayerChatEvent, SPlayerChatEvent> {

    public ChatEventHandlerFactory(Object plugin, ProxyServer proxyServer) {
        super(PlayerChatEvent.class, SPlayerChatEvent.class, plugin, proxyServer);
    }

    @Override
    protected SPlayerChatEvent wrapEvent(PlayerChatEvent event, EventPriority priority) {
        return new SPlayerChatEvent(
                ProxiedPlayerMapper.wrapPlayer(event.getPlayer()),
                event.getMessage().startsWith("/"),
                event.getResult().getMessage().orElse(event.getMessage()),
                event.getResult().isAllowed()
        );
    }

    @Override
    protected void postProcess(SPlayerChatEvent wrappedEvent, PlayerChatEvent event) {
        if (wrappedEvent.isCancelled()) {
            event.setResult(PlayerChatEvent.ChatResult.denied());
        } else {
            event.setResult(PlayerChatEvent.ChatResult.message(wrappedEvent.getMessage()));
        }
    }
}
