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
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.*;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractVelocityEventHandlerFactory<T, SE extends Event> {
    protected static final @NotNull Map<@NotNull EventExecutionOrder, PostOrder> EVENT_PRIORITY_POST_ORDER_MAP = Map.of(
            EventExecutionOrder.FIRST, PostOrder.FIRST,
            EventExecutionOrder.EARLY, PostOrder.EARLY,
            EventExecutionOrder.NORMAL, PostOrder.NORMAL,
            EventExecutionOrder.LATE, PostOrder.LATE,
            EventExecutionOrder.LAST, PostOrder.LAST
    );

    protected final @NotNull Map<@NotNull EventExecutionOrder, EventHandler<T>> eventMap = new HashMap<>();
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

            var priority = handlerRegisteredEvent.getHandler().getExecutionOrder();
            if (priority == EventExecutionOrder.MONITOR) {
                priority = EventExecutionOrder.LAST;
            }
            if (!eventMap.containsKey(priority)) {
                final var finalPriority = priority;
                final EventHandler<T> handler = event -> {
                    final var wrapped = wrapEvent(event, finalPriority);
                    if (wrapped == null) {
                        return;
                    }

                    if (this.fireAsync) {
                        try {
                            EventManager.getDefaultEventManager().fireEventAsync(wrapped, finalPriority).get();
                            if (finalPriority == EventExecutionOrder.LAST) {
                                EventManager.getDefaultEventManager().fireEventAsync(wrapped, EventExecutionOrder.MONITOR).get();
                            }
                        } catch (Throwable throwable) {
                            throw new RuntimeException(throwable);
                        }
                    } else {
                        EventManager.getDefaultEventManager().fireEvent(wrapped, finalPriority);
                        if (finalPriority == EventExecutionOrder.LAST) {
                            EventManager.getDefaultEventManager().fireEvent(wrapped, EventExecutionOrder.MONITOR);
                        }
                    }
                };

                eventMap.put(priority, handler);
                proxyServer.getEventManager().register(plugin, this.platformEventClass, EVENT_PRIORITY_POST_ORDER_MAP.get(priority), handler);
            }
        });
    }

    protected abstract @Nullable SE wrapEvent(@NotNull T event, @NotNull EventExecutionOrder priority);
}
