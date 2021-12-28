package org.screamingsandals.lib.bukkit.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.event.*;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBukkitEventHandlerFactory<T extends Event, SE extends SEvent> implements Listener {

    protected final Map<EventPriority, EventExecutor> eventMap = new HashMap<>();
    protected final boolean fireAsync;
    protected final boolean checkOnlySameNotChildren;
    protected final Class<SE> eventClass;
    protected final Class<T> platformEventClass;

    public AbstractBukkitEventHandlerFactory(Class<T> platformEventClass, Class<SE> eventClass, final Plugin plugin) {
        this(platformEventClass, eventClass, plugin, false);
    }

    public AbstractBukkitEventHandlerFactory(Class<T> platformEventClass, Class<SE> eventClass, final Plugin plugin, boolean fireAsync) {
        this(platformEventClass, eventClass, plugin, fireAsync, false);
    }

    @SuppressWarnings("unchecked")
    public AbstractBukkitEventHandlerFactory(Class<T> platformEventClass, Class<SE> eventClass, final Plugin plugin, boolean fireAsync, boolean checkOnlySameNotChildren) {
        this.eventClass = eventClass;
        this.platformEventClass = platformEventClass;
        this.fireAsync = fireAsync;
        this.checkOnlySameNotChildren = checkOnlySameNotChildren;

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
                    if (checkOnlySameNotChildren) {
                        if (!platformEventClass.equals(event.getClass())) {
                            return;
                        }
                    } else if (!platformEventClass.isInstance(event)) {
                        return;
                    }

                    final var wrapped = wrapEvent((T) event, priority);
                    if (wrapped == null) {
                        return;
                    }

                    if (!(event instanceof NoAutoCancellable) && wrapped instanceof Cancellable && event instanceof org.bukkit.event.Cancellable) {
                        ((Cancellable) wrapped).setCancelled(((org.bukkit.event.Cancellable) event).isCancelled());
                    }

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

                    if (!(event instanceof NoAutoCancellable) && wrapped instanceof Cancellable && event instanceof org.bukkit.event.Cancellable) {
                        final var isCancelled = ((Cancellable) wrapped).isCancelled();
                        ((org.bukkit.event.Cancellable) event).setCancelled(isCancelled);
                    }
                };

                eventMap.put(handlerRegisteredEvent.getHandler().getEventPriority(), handler);
                Bukkit.getPluginManager().registerEvent(this.platformEventClass, this, org.bukkit.event.EventPriority.valueOf(priority.name()), handler, plugin);
            }
        });
    }

    /**
     * Wrapping the platform event to the ScreamingLib event
     * @param event event to wrap
     * @param priority priority
     * @return wrapped event or null (if null, nothing happens)
     */
    protected abstract SE wrapEvent(T event, EventPriority priority);
}