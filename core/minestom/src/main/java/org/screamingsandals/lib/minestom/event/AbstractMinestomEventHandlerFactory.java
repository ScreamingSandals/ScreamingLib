package org.screamingsandals.lib.minestom.event;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.extensions.Extension;
import org.screamingsandals.lib.event.*;

import java.util.function.Consumer;

public abstract class AbstractMinestomEventHandlerFactory<T extends Event, SE extends SEvent> {
    protected final boolean checkOnlySameNotChildren;
    protected final Class<SE> eventClass;
    protected final Class<T> platformEventClass;

    public AbstractMinestomEventHandlerFactory(Class<T> platformEventClass, Class<SE> eventClass, final Extension plugin) {
        this(platformEventClass, eventClass, plugin, false);
    }

    public AbstractMinestomEventHandlerFactory(Class<T> platformEventClass, Class<SE> eventClass, final Extension plugin, boolean checkOnlySameNotChildren) {
        this.eventClass = eventClass;
        this.platformEventClass = platformEventClass;
        this.checkOnlySameNotChildren = checkOnlySameNotChildren;

        EventManager.getDefaultEventManager().register(HandlerRegisteredEvent.class, handlerRegisteredEvent -> {
            if (handlerRegisteredEvent.getEventManager() != EventManager.getDefaultEventManager()) {
                return;
            }

            if (!this.eventClass.isAssignableFrom(handlerRegisteredEvent.getEventClass())) {
                return;
            }

            final Consumer<T> handler = event -> {
                final var wrapped = wrapEvent(event);
                if (wrapped == null) {
                    return;
                }

                if (wrapped instanceof Cancellable && event instanceof CancellableEvent) {
                    ((Cancellable) wrapped).setCancelled(((CancellableEvent) event).isCancelled());
                }

                try {
                    EventManager.getDefaultEventManager().fireEvent(wrapped);
                } catch (Throwable throwable) {
                    throw new EventException(throwable);
                }

                if (wrapped instanceof Cancellable && event instanceof CancellableEvent) {
                    ((CancellableEvent) event).setCancelled(((Cancellable) wrapped).isCancelled());
                }
            };
            plugin.getEventNode().addListener(
                    EventListener.builder(platformEventClass)
                            .handler(handler)
                            .ignoreCancelled(handlerRegisteredEvent.getHandler().isIgnoreCancelled())
                            .filter(event -> {
                                if (checkOnlySameNotChildren) {
                                    return platformEventClass.equals(event.getClass());
                                }
                                return platformEventClass.isInstance(event);
                            })
                            .build()
            );
        });
    }

    protected abstract SE wrapEvent(T event);
}
