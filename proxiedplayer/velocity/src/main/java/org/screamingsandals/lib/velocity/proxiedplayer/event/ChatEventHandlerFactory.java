package org.screamingsandals.lib.velocity.proxiedplayer.event;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.proxy.ProxyServer;
import org.screamingsandals.lib.proxiedplayer.ProxiedPlayerMapper;
import org.screamingsandals.lib.proxiedplayer.event.PlayerChatEvent;
import org.screamingsandals.lib.utils.event.*;

import java.util.HashMap;
import java.util.Map;

public class ChatEventHandlerFactory {
    private static final Map<EventPriority, PostOrder> EVENT_PRIORITY_POST_ORDER_MAP = Map.of(
            EventPriority.LOWEST, PostOrder.FIRST,
            EventPriority.LOW, PostOrder.EARLY,
            EventPriority.NORMAL, PostOrder.NORMAL,
            EventPriority.HIGH, PostOrder.LATE,
            EventPriority.HIGHEST, PostOrder.LAST
    );

    private final Map<EventPriority, com.velocitypowered.api.event.EventHandler<com.velocitypowered.api.event.player.PlayerChatEvent>> map = new HashMap<>();

    public ChatEventHandlerFactory(final Object plugin, final ProxyServer proxyServer) {
        EventManager.getBaseEventManager().register(HandlerRegisteredEvent.class, handlerRegisteredEvent -> {
            if (handlerRegisteredEvent.getEventManager() != EventManager.getBaseEventManager()) {
                return;
            }

            if (!PlayerChatEvent.class.isAssignableFrom(handlerRegisteredEvent.getEventClass())) {
                return;
            }

            if (!map.containsKey(handlerRegisteredEvent.getHandler().getEventPriority())) {
                final com.velocitypowered.api.event.EventHandler<com.velocitypowered.api.event.player.PlayerChatEvent> handler = event -> {
                    var wrapEvent = new PlayerChatEvent(ProxiedPlayerMapper.wrapPlayer(event.getPlayer()), event.getResult().getMessage().orElse(event.getMessage()), event.getResult().isAllowed(), event.getMessage().startsWith("/"));
                    //noinspection unchecked
                    EventManager.getBaseEventManager().fireEvent(wrapEvent);
                    if (!wrapEvent.isCancelled()) {
                        event.setResult(com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult.message(wrapEvent.getMessage()));
                    } else {
                        event.setResult(com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult.denied());
                    }
                };
                //noinspection unchecked
                map.put(handlerRegisteredEvent.getHandler().getEventPriority(), handler);
                proxyServer.getEventManager().register(plugin, com.velocitypowered.api.event.player.PlayerChatEvent.class, EVENT_PRIORITY_POST_ORDER_MAP.get(handlerRegisteredEvent.getHandler().getEventPriority()), handler);
            }
        });
    }
}
