/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.lib.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.ReceiverConsumer;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.executor.ExecutorProvider;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Custom event manager that has its own {@link java.util.concurrent.ExecutorService}.
 * <p>
 * Always call {@link EventManager#destroy()} when shutting down!
 */
@AbstractService
@NoArgsConstructor
public abstract class EventManager {
    private static ExecutorService executor;
    @Getter
    private static EventManager defaultEventManager;
    private final Map<Class<?>, List<EventHandler<? extends SEvent>>> handlers = new ConcurrentHashMap<>();
    @Getter
    private EventManager customManager;

    public EventManager(Controllable controllable) {
        controllable
                .enable(() -> executor = ExecutorProvider.buildExecutor("SEventManager"))
                .preDisable(this::destroy);
    }

    public static void init(Supplier<EventManager> supplier) {
        if (defaultEventManager != null) {
            throw new UnsupportedOperationException("Default EventManager has been already initialized!");
        }
        defaultEventManager = supplier.get();
    }


    public static <K extends SEvent> K fire(K event) {
        return defaultEventManager.fireEvent(event);
    }

    public static <K extends SEvent> CompletableFuture<K> fireAsync(K event) {
        return defaultEventManager.fireEventAsync(event);
    }

    public static EventManager createChildManager() {
        return new EventManager() {
            @Override
            public boolean isServerThread() {
                return defaultEventManager.isServerThread();
            }
        };
    }

    public static boolean isDefaultInitialized() {
        return defaultEventManager != null;
    }

    public <T extends SEvent> EventHandler<T> register(Class<T> event, ReceiverConsumer<T> consumer) {
        return register(event, EventHandler.of(consumer));
    }

    public <T extends SEvent> EventHandler<T> registerOneTime(Class<T> event, Function<T, Boolean> function) {
        return register(event, EventHandler.ofOneTime(handler -> e -> {
            if (function.apply(e)) {
                unregister(handler);
            }
        }));
    }

    public <T extends SEvent> EventHandler<T> register(Class<T> event, ReceiverConsumer<T> consumer, boolean ignoreCancelled) {
        return register(event, EventHandler.of(consumer, ignoreCancelled));
    }

    public <T extends SEvent> EventHandler<T> registerOneTime(Class<T> event, Function<T, Boolean> function, boolean ignoreCancelled) {
        return register(event, EventHandler.ofOneTime(handler -> e -> {
            if (function.apply(e)) {
                unregister(handler);
            }
        }, ignoreCancelled));
    }

    public <T extends SEvent> EventHandler<T> register(Class<T> event, ReceiverConsumer<T> consumer, EventPriority eventPriority) {
        return register(event, EventHandler.of(consumer, eventPriority));
    }

    public <T extends SEvent> EventHandler<T> registerOneTime(Class<T> event, Function<T, Boolean> function, EventPriority eventPriority,
                                                              boolean ignoreCancelled) {
        return register(event, EventHandler.ofOneTime(handler -> e -> {
            if (function.apply(e)) {
                unregister(handler);
            }
        }, eventPriority, ignoreCancelled));
    }

    public <T extends SEvent> EventHandler<T> register(Class<T> event, ReceiverConsumer<T> consumer, EventPriority eventPriority,
                                                       boolean ignoreCancelled) {
        return register(event, EventHandler.of(consumer, eventPriority, ignoreCancelled));
    }

    public <T extends SEvent> EventHandler<T> register(Class<T> event, EventHandler<T> handler) {
        handlers.computeIfAbsent(event, e -> Collections.synchronizedList(new ArrayList<>())).add(handler);
        fireEvent(new HandlerRegisteredEvent(this, event, handler));

        return handler;
    }

    public <T extends SEvent> void unregister(EventHandler<T> handler) {
        handlers.forEach((key, value) -> {
            if (value.contains(handler)) {
                fireEvent(new HandlerUnregisteredEvent(this, key, handler));
                value.remove(handler);
                if (value.isEmpty()) {
                    handlers.remove(key, value);
                }
            }
        });
    }

    public <K extends SEvent> K fireEvent(@NotNull K event) {
        EventPriority.VALUES.forEach(priority -> fireEvent(event, priority));
        return event;
    }

    public <K extends SEvent> K fireEvent(@NotNull K event, @NotNull EventPriority eventPriority) {
        if (event.isAsync() && isServerThread()) {
            throw new UnsupportedOperationException("Async event cannot be fired sync!");
        }

        findEventHandlers(event, eventPriority)
                .forEach(eventHandler -> eventHandler.fire(event));

        if (customManager != null) {
            customManager.fireEvent(event, eventPriority);
        } else if (this != defaultEventManager) {
            defaultEventManager.fireEvent(event, eventPriority);
        }

        return event;
    }

    public <K extends SEvent> CompletableFuture<K> fireEventAsync(@NotNull K event) {
        final var futures = new LinkedList<CompletableFuture<K>>();
        EventPriority.VALUES.forEach(priority -> futures.add(fireEventAsync(event, priority)));

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(ignored -> event);
    }

    public <K extends SEvent> CompletableFuture<K> fireEventAsync(@NotNull K event, @NotNull EventPriority eventPriority) {
        if (!event.isAsync()) {
            //we don't want non-async events to be called async. :)
            return CompletableFuture.completedFuture(fireEvent(event, eventPriority));
        }

        final var futures = findEventHandlers(event, eventPriority)
                .map(eventHandler -> {
                    // checks for server thread
                    if (isServerThread()) {
                        return CompletableFuture.runAsync(() -> eventHandler.fire(event), executor)
                                .exceptionally(ex -> {
                                    throw new RuntimeException("Exception occurred while firing event!", ex);
                                });
                    }
                    return CompletableFuture.completedFuture(fireEvent(event, eventPriority));
                })
                .collect(Collectors.toCollection(LinkedList::new));

        if (customManager != null) {
            futures.add(customManager.fireEventAsync(event, eventPriority)
                    .thenApply(ignored -> null));
        } else if (this != defaultEventManager) {
            futures.add(defaultEventManager.fireEventAsync(event, eventPriority)
                    .thenApply(ignored -> null));
        }

        if (futures.isEmpty()) {
            return CompletableFuture.completedFuture(event);
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(ignored -> event);
    }

    public void unregisterAll() {
        handlers.entrySet().stream().flatMap(entry -> entry.getValue().stream()).forEach(this::unregister);
    }

    public void drop() {
        handlers.clear();
    }

    public void setCustomManager(EventManager parent) {
        if (this != defaultEventManager) {
            this.customManager = parent;
        }
    }

    public boolean isRegistered(EventHandler<?> eventHandler) {
        return handlers.entrySet().stream()
                .anyMatch(entry -> entry.getValue().contains(eventHandler));
    }

    @SuppressWarnings("unchecked")
    public void cloneEventManager(EventManager originalEventManager) {
        originalEventManager.handlers.forEach((key, value) -> value.forEach(entry1 -> register((Class<SEvent>) key, (EventHandler<SEvent>) entry1)));
    }

    public void destroy() {
        handlers.clear();

        if (this == defaultEventManager) {
            ExecutorProvider.destroyExecutor(executor);
        }
    }

    private <E extends SEvent> Stream<? extends EventHandler<? extends SEvent>> findEventHandlers(E event, EventPriority priority) {
        return handlers.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isInstance(event))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .filter(eventHandler -> eventHandler.getEventPriority() == priority);
    }

    public abstract boolean isServerThread();
}
