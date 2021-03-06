package org.screamingsandals.lib.velocity.event;

import com.velocitypowered.api.event.EventHandler;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import org.screamingsandals.lib.event.*;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractVelocityEventHandlerFactory<T, SE extends AbstractEvent> {
    protected static final Map<EventPriority, PostOrder> EVENT_PRIORITY_POST_ORDER_MAP = Map.of(
            EventPriority.LOWEST, PostOrder.FIRST,
            EventPriority.LOW, PostOrder.EARLY,
            EventPriority.NORMAL, PostOrder.NORMAL,
            EventPriority.HIGH, PostOrder.LATE,
            EventPriority.HIGHEST, PostOrder.LAST
    );

    protected final Map<EventPriority, EventHandler<T>> eventMap = new HashMap<>();
    protected final Class<T> platformEventClass;
    protected final Class<SE> eventClass;
    protected final boolean fireAsync;

    public AbstractVelocityEventHandlerFactory(Class<T> platformEventClass, Class<SE> eventClass,
                                               final Object plugin, final ProxyServer proxyServer) {
        this(platformEventClass, eventClass, plugin, proxyServer, false);
    }

    @SuppressWarnings("unchecked")
    public AbstractVelocityEventHandlerFactory(Class<T> platformEventClass, Class<SE> eventClass,
                                               final Object plugin, final ProxyServer proxyServer,
                                               boolean fireAsync) {
        this.eventClass = eventClass;
        this.platformEventClass = platformEventClass;
        this.fireAsync = fireAsync;

        EventManager.getDefaultEventManager().register(HandlerRegisteredEvent.class, handlerRegisteredEvent -> {
            if (handlerRegisteredEvent.getEventManager() != EventManager.getDefaultEventManager()) {
                return;
            }

            if (!this.eventClass.isAssignableFrom(handlerRegisteredEvent.getEventClass())) {
                return;
            }

            final var priority = handlerRegisteredEvent.getHandler().getEventPriority();
            if (!eventMap.containsKey(priority)) {
                final EventHandler<T> handler = event -> {
                    final var wrapped = wrapEvent(event, priority);
                    if (this.fireAsync) {
                        try {
                            EventManager.getDefaultEventManager().fireEventAsync(wrapped, priority).get();
                        } catch (Throwable throwable) {
                            throw new RuntimeException(throwable);
                        }
                    } else {
                        EventManager.getDefaultEventManager().fireEvent(wrapped, priority);
                    }
                    if (wrapped instanceof CancellableAbstractEvent
                            && event instanceof ResultedEvent) {
                        final var isCancelled = ((CancellableAbstractEvent) wrapped).isCancelled();
                        if (isCancelled) {
                            ((ResultedEvent<ResultedEvent.Result>) event).setResult(ResultedEvent.GenericResult.denied());
                        } else {
                            ((ResultedEvent<ResultedEvent.Result>) event).setResult(ResultedEvent.GenericResult.allowed());
                        }
                    }

                    postProcess(wrapped, event);
                };

                eventMap.put(handlerRegisteredEvent.getHandler().getEventPriority(), handler);
                proxyServer.getEventManager()
                        .register(plugin, this.platformEventClass, EVENT_PRIORITY_POST_ORDER_MAP.get(priority), handler);
            }
        });
    }

    protected abstract SE wrapEvent(T event, EventPriority priority);

    /**
     * For additional processing of the event.
     * If the event is instance of {@link com.velocitypowered.api.event.ResultedEvent}, the result is set from our wrapped event
     *
     * @param wrappedEvent wrapped event
     * @param event        velocity event
     */
    protected abstract void postProcess(SE wrappedEvent, T event);
}
