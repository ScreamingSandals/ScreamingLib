package org.screamingsandals.lib.bukkit.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.HandlerRegisteredEvent;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBukkitEventHandlerFactory<T extends Event, SE extends AbstractEvent> implements Listener {

    protected final Map<EventPriority, EventExecutor> eventMap = new HashMap<>();
    protected final boolean fireAsync;
    protected final Class<SE> eventClass;
    protected final Class<T> platformEventClass;

    public AbstractBukkitEventHandlerFactory(Class<T> platformEventClass, Class<SE> eventClass, final Plugin plugin) {
        this(platformEventClass, eventClass, plugin, false);
    }

    @SuppressWarnings("unchecked")
    public AbstractBukkitEventHandlerFactory(Class<T> platformEventClass, Class<SE> eventClass, final Plugin plugin, boolean fireAsync) {
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
                final EventExecutor handler = (listener, event) -> {
                    final var wrapped = wrapEvent((T) event, priority);
                    if (this.fireAsync) {
                        try {
                            EventManager.getDefaultEventManager().fireEventAsync(wrapped, priority).get();
                        } catch (Throwable throwable) {
                            throw new EventException(throwable);
                        }
                    } else {
                       try {
                           EventManager.getDefaultEventManager().fireEvent(wrapped, priority);
                       } catch (Throwable throwable) {
                           throw new EventException(throwable);
                       }
                    }
                    handleResult(wrapped, (T) event);
                };

                eventMap.put(handlerRegisteredEvent.getHandler().getEventPriority(), handler);
                Bukkit.getPluginManager().registerEvent(this.platformEventClass, this, org.bukkit.event.EventPriority.valueOf(priority.name()), handler, plugin);
            }
        });
    }

    protected abstract SE wrapEvent(T event, EventPriority priority);

    protected void handleResult(SE wrappedEvent, T event) {}
}
