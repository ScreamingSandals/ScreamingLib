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

package org.screamingsandals.lib.bukkit.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.*;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBukkitEventHandlerFactory<T extends Event, SE extends SEvent> implements Listener {

    protected final @NotNull Map<@NotNull EventPriority, EventExecutor> eventMap = new HashMap<>();
    protected final boolean fireAsync;
    protected final boolean checkOnlySameNotChildren;
    protected final @NotNull Class<SE> eventClass;
    protected final @NotNull Class<T> platformEventClass;

    public AbstractBukkitEventHandlerFactory(@NotNull Class<T> platformEventClass, @NotNull Class<SE> eventClass, @NotNull Plugin plugin) {
        this(platformEventClass, eventClass, plugin, false);
    }

    public AbstractBukkitEventHandlerFactory(@NotNull Class<T> platformEventClass, @NotNull Class<SE> eventClass, @NotNull Plugin plugin, boolean fireAsync) {
        this(platformEventClass, eventClass, plugin, fireAsync, false);
    }

    @SuppressWarnings("unchecked")
    public AbstractBukkitEventHandlerFactory(@NotNull Class<T> platformEventClass, @NotNull Class<SE> eventClass, @NotNull Plugin plugin, boolean fireAsync, boolean checkOnlySameNotChildren) {
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

                    if (!(wrapped instanceof NoAutoCancellable) && wrapped instanceof Cancellable && event instanceof org.bukkit.event.Cancellable) {
                        ((Cancellable) wrapped).cancelled(((org.bukkit.event.Cancellable) event).isCancelled());
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

                    if (!(wrapped instanceof NoAutoCancellable) && wrapped instanceof Cancellable && event instanceof org.bukkit.event.Cancellable) {
                        final var isCancelled = ((Cancellable) wrapped).cancelled();
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
    protected abstract @Nullable SE wrapEvent(@NotNull T event, @NotNull EventPriority priority);
}
