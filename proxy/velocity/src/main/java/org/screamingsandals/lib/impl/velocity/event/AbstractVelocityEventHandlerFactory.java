/*
 * Copyright 2023 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.impl.velocity.event;

import com.velocitypowered.api.event.EventHandler;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.event.*;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractVelocityEventHandlerFactory<T, SE extends Event> {
    protected static final @NotNull Map<@NotNull EventPriority, PostOrder> EVENT_PRIORITY_POST_ORDER_MAP = Map.of(
            EventPriority.LOWEST, PostOrder.FIRST,
            EventPriority.LOW, PostOrder.EARLY,
            EventPriority.NORMAL, PostOrder.NORMAL,
            EventPriority.HIGH, PostOrder.LATE,
            EventPriority.HIGHEST, PostOrder.LAST
    );

    protected final @NotNull Map<@NotNull EventPriority, EventHandler<T>> eventMap = new HashMap<>();
    protected final @NotNull Class<T> platformEventClass;
    protected final @NotNull Class<SE> eventClass;
    protected final boolean fireAsync;

    public AbstractVelocityEventHandlerFactory(
            @NotNull Class<T> platformEventClass,
            @NotNull Class<SE> eventClass,
            final @NotNull Object plugin,
            final @NotNull ProxyServer proxyServer
    ) {
        this(platformEventClass, eventClass, plugin, proxyServer, false);
    }

    @SuppressWarnings("unchecked")
    public AbstractVelocityEventHandlerFactory(
            @NotNull Class<T> platformEventClass,
            @NotNull Class<SE> eventClass,
            final @NotNull Object plugin,
            final @NotNull ProxyServer proxyServer,
            boolean fireAsync
    ) {
        this.eventClass = eventClass;
        this.platformEventClass = platformEventClass;
        this.fireAsync = fireAsync;

        if (EventManager.getDefaultEventManager() == null) {
            throw new UnsupportedOperationException("Default EventManager is not initialized yet.");
        }

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
                    if (wrapped instanceof Cancellable
                            && event instanceof ResultedEvent && !(wrapped instanceof NoAutoCancellable)) {
                        final var isCancelled = ((Cancellable) wrapped).cancelled();
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

    protected abstract @NotNull SE wrapEvent(@NotNull T event, @NotNull EventPriority priority);

    /**
     * For additional processing of the event.
     * If the event is instance of {@link com.velocitypowered.api.event.ResultedEvent}, the result is set from our wrapped event
     *
     * @param wrappedEvent wrapped event
     * @param event        velocity event
     */
    protected void postProcess(@NotNull SE wrappedEvent, @NotNull T event) {
    }
}