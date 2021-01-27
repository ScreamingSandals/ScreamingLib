package org.screamingsandals.lib.velocity.event;

import com.velocitypowered.api.event.EventHandler;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.HandlerRegisteredEvent;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractEventHandlerFactory<T extends ResultedEvent<?>, SE extends CancellableAbstractEvent> {
    protected static final Map<EventPriority, PostOrder> EVENT_PRIORITY_POST_ORDER_MAP = Map.of(
            EventPriority.LOWEST, PostOrder.FIRST,
            EventPriority.LOW, PostOrder.EARLY,
            EventPriority.NORMAL, PostOrder.NORMAL,
            EventPriority.HIGH, PostOrder.LATE,
            EventPriority.HIGHEST, PostOrder.LAST
    );

    protected final Map<EventPriority, EventHandler<T>> eventMap = new HashMap<>();
    protected final Class<T> eventClass;

    public AbstractEventHandlerFactory(Class<T> eventClass, final Object plugin, final ProxyServer proxyServer) {
        this.eventClass = eventClass;

        EventManager.getDefaultEventManager().register(HandlerRegisteredEvent.class, handlerRegisteredEvent -> {
            if (handlerRegisteredEvent.getEventManager() != EventManager.getDefaultEventManager()) {
                return;
            }

            if (!eventClass.isAssignableFrom(handlerRegisteredEvent.getEventClass())) {
                return;
            }

            final var priority = handlerRegisteredEvent.getHandler().getEventPriority();
            if (!eventMap.containsKey(priority)) {
                final EventHandler<T> handler = event -> {
                    final var wrapped = wrapEvent(event, handlerRegisteredEvent.getHandler().getEventPriority());
                    EventManager.getDefaultEventManager().fireEvent(wrapped);
                    handleResult(wrapped, event);
                };

                eventMap.put(handlerRegisteredEvent.getHandler().getEventPriority(), handler);
                proxyServer.getEventManager()
                        .register(plugin, eventClass, EVENT_PRIORITY_POST_ORDER_MAP.get(priority), handler);
            }
        });
    }

    protected abstract SE wrapEvent(T event, EventPriority priority);

    protected abstract void handleResult(SE wrappedEvent, T event);
}
