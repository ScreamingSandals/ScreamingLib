/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.impl.bungee.event;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractBungeeEventHandlerFactory<T, SE extends Event> {
    protected final @NotNull Map<@NotNull EventExecutionOrder, Listener> eventMap = new HashMap<>();
    protected final @NotNull Class<T> platformEventClass;
    protected final @NotNull Class<SE> eventClass;
    protected final boolean fireAsync;

    public AbstractBungeeEventHandlerFactory(
            @NotNull Class<T> platformEventClass,
            @NotNull Class<SE> eventClass,
            final @NotNull Plugin plugin,
            final @NotNull PluginManager pluginManager
    ) {
        this(platformEventClass, eventClass, plugin, pluginManager, false);
    }

    public AbstractBungeeEventHandlerFactory(
            @NotNull Class<T> platformEventClass,
            @NotNull Class<SE> eventClass,
            final @NotNull Plugin plugin,
            final @NotNull PluginManager pluginManager,
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

            final var priority = handlerRegisteredEvent.getHandler().getExecutionOrder();
            if (!eventMap.containsKey(priority)) {
                final @NotNull Consumer<@NotNull T> handler = event -> {
                    final var wrapped = wrapEvent(event, priority);
                    if (wrapped == null) {
                        return;
                    }

                    if (this.fireAsync) {
                        try {
                            EventManager.getDefaultEventManager().fireEventAsync(wrapped, priority).get();
                        } catch (Throwable throwable) {
                            throw new RuntimeException(throwable);
                        }
                    } else {
                        EventManager.getDefaultEventManager().fireEvent(wrapped, priority);
                    }
                };

                @NotNull Listener listener;

                switch (priority) {
                    case FIRST:
                        listener = constructLowestPriorityHandler(handler);
                        break;
                    case EARLY:
                        listener = constructLowPriorityHandler(handler);
                        break;
                    default:
                    case NORMAL:
                        listener = constructNormalPriorityHandler(handler);
                        break;
                    case LATE:
                        listener = constructHighPriorityHandler(handler);
                        break;
                    case LAST:
                        listener = constructHighestPriorityHandler(handler);
                        break;
                    case MONITOR:
                        listener = constructMonitorPriorityHandler(handler);
                        break;
                }

                eventMap.put(priority, listener);
                pluginManager.registerListener(plugin, listener);
            }
        });
    }

    protected abstract @Nullable SE wrapEvent(@NotNull T event, @NotNull EventExecutionOrder priority);

    // BungeeCord does not have a way to register EventHandler directly. Which is sad.

    protected abstract @NotNull Listener constructLowestPriorityHandler(@NotNull Consumer<@NotNull T> handler);

    protected abstract @NotNull Listener constructLowPriorityHandler(@NotNull Consumer<@NotNull T> handler);

    protected abstract @NotNull Listener constructNormalPriorityHandler(@NotNull Consumer<@NotNull T> handler);

    protected abstract @NotNull Listener constructHighPriorityHandler(@NotNull Consumer<@NotNull T> handler);

    protected abstract @NotNull Listener constructHighestPriorityHandler(@NotNull Consumer<@NotNull T> handler);

    protected abstract @NotNull Listener constructMonitorPriorityHandler(@NotNull Consumer<@NotNull T> handler);
}
