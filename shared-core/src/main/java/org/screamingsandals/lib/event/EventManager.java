/*
 * Copyright 2026 ScreamingSandals
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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.ReceiverConsumer;
import org.screamingsandals.lib.utils.annotations.ProvidedService;
import org.screamingsandals.lib.impl.utils.executor.ExecutorProvider;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Custom event manager that has its own {@link java.util.concurrent.ExecutorService}.
 * <p>
 * Always call {@link EventManager#destroy()} when shutting down!
 */
@ProvidedService
@NoArgsConstructor
public abstract class EventManager {
    private static @Nullable ExecutorService executor;
    @Getter
    private static @Nullable EventManager defaultEventManager;
    private final @NotNull Map<@NotNull Class<?>, List<@NotNull EventHandler<? extends Event>>> handlers = new ConcurrentHashMap<>();
    private final @NotNull Map<@NotNull Class<?>, @NotNull EnumMap<EventExecutionOrder, List<EventHandler<? extends Event>>>> cache = new ConcurrentHashMap<>();
    @Getter
    private @Nullable EventManager customManager;

    public EventManager(Controllable controllable) {
        controllable
                .enable(() -> executor = ExecutorProvider.buildExecutor("SEventManager"))
                .preDisable(this::destroy);
    }

    @ApiStatus.Internal
    public static void init(@NotNull Supplier<@NotNull EventManager> supplier) {
        if (defaultEventManager != null) {
            throw new UnsupportedOperationException("Default EventManager has been already initialized!");
        }
        defaultEventManager = supplier.get();
    }


    public static <K extends Event> @NotNull K fire(@NotNull K event) {
        if (defaultEventManager == null) {
            throw new UnsupportedOperationException("Default EventManager has not been initialized yet!");
        }
        return defaultEventManager.fireEvent(event);
    }

    public static <K extends Event> @NotNull CompletableFuture<@NotNull K> fireAsync(@NotNull K event) {
        if (defaultEventManager == null) {
            throw new UnsupportedOperationException("Default EventManager has not been initialized yet!");
        }
        return defaultEventManager.fireEventAsync(event);
    }

    public static @NotNull EventManager createChildManager() {
        if (defaultEventManager == null) {
            throw new UnsupportedOperationException("Default EventManager has not been initialized yet!");
        }
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

    public <T extends Event> @NotNull EventHandler<T> register(@NotNull Class<T> event, @NotNull ReceiverConsumer<@NotNull T> consumer) {
        return register(event, EventHandler.of(consumer));
    }

    public <T extends Event> @NotNull EventHandler<T> registerOneTime(@NotNull Class<T> event, @NotNull Function<@NotNull T, @NotNull Boolean> function) {
        return register(event, EventHandler.ofOneTime(handler -> e -> {
            if (function.apply(e)) {
                unregister(handler);
            }
        }));
    }

    public <T extends Event> @NotNull EventHandler<T> register(@NotNull Class<T> event, @NotNull ReceiverConsumer<@NotNull T> consumer, boolean ignoreCancelled) {
        return register(event, EventHandler.of(consumer, ignoreCancelled));
    }

    public <T extends Event> @NotNull EventHandler<T> registerOneTime(@NotNull Class<T> event, @NotNull Function<@NotNull T, @NotNull Boolean> function, boolean ignoreCancelled) {
        return register(event, EventHandler.ofOneTime(handler -> e -> {
            if (function.apply(e)) {
                unregister(handler);
            }
        }, ignoreCancelled));
    }

    public <T extends Event> @NotNull EventHandler<T> register(@NotNull Class<T> event, @NotNull ReceiverConsumer<@NotNull T> consumer, @NotNull EventExecutionOrder eventPriority) {
        return register(event, EventHandler.of(consumer, eventPriority));
    }

    public <T extends Event> @NotNull EventHandler<T> registerOneTime(@NotNull Class<T> event, @NotNull Function<@NotNull T, @NotNull Boolean> function, @NotNull EventExecutionOrder eventPriority, boolean ignoreCancelled) {
        return register(event, EventHandler.ofOneTime(handler -> e -> {
            if (function.apply(e)) {
                unregister(handler);
            }
        }, eventPriority, ignoreCancelled));
    }

    public <T extends Event> @NotNull EventHandler<T> register(@NotNull Class<T> event, @NotNull ReceiverConsumer<@NotNull T> consumer, @NotNull EventExecutionOrder eventPriority,
                                                               boolean ignoreCancelled) {
        return register(event, EventHandler.of(consumer, eventPriority, ignoreCancelled));
    }

    public <T extends Event> @NotNull EventHandler<T> register(@NotNull Class<T> event, @NotNull EventHandler<T> handler) {
        handlers.computeIfAbsent(event, k -> new CopyOnWriteArrayList<>()).add(handler);
        cache.clear();

        fireEvent(new HandlerRegisteredEvent(this, event, handler));
        return handler;
    }

    public <T extends Event> void unregister(@NotNull EventHandler<T> handler) {
        handlers.forEach((key, value) -> {
            if (value.contains(handler)) {
                fireEvent(new HandlerUnregisteredEvent(this, key, handler));
                value.remove(handler);
                cache.clear();
                if (value.isEmpty()) {
                    handlers.remove(key, value);
                }
            }
        });
    }

    public <K extends Event> @NotNull K fireEvent(@NotNull K event) {
        for (var order : EventExecutionOrder.VALUES) {
            fireEvent(event, order);
        }
        return event;
    }

    public <K extends Event> @NotNull K fireEvent(@NotNull K event, @NotNull EventExecutionOrder executionOrder) {
        if (event.isAsync() && isServerThread() && executor != null) {
            throw new UnsupportedOperationException("Async event cannot be fired sync!");
        }

        var handlers = resolveHandlers(event.getClass(), executionOrder);

        for (var handler : handlers) {
            handler.fire(event);
        }

        if (customManager != null) {
            customManager.fireEvent(event, executionOrder);
        } else if (this != defaultEventManager && defaultEventManager != null) {
            defaultEventManager.fireEvent(event, executionOrder);
        }

        return event;
    }

    public <K extends Event> @NotNull CompletableFuture<@NotNull K> fireEventAsync(@NotNull K event) {
        final var futures = new LinkedList<CompletableFuture<K>>();
        EventExecutionOrder.VALUES.forEach(priority -> futures.add(fireEventAsync(event, priority)));

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(ignored -> event);
    }

    public <K extends Event> @NotNull CompletableFuture<@NotNull K> fireEventAsync(@NotNull K event, @NotNull EventExecutionOrder executionOrder) {
        if (!event.isAsync() || executor == null) {
            //we don't want non-async events to be called async. :)
            return CompletableFuture.completedFuture(fireEvent(event, executionOrder));
        }

        final var handlers = resolveHandlers(event.getClass(), executionOrder);
        final var futures = new ArrayList<CompletableFuture<?>>(handlers.size());

        for (var handler : handlers) {
            if (isServerThread()) {
                futures.add(
                        CompletableFuture.runAsync(() -> handler.fire(event), executor)
                            .exceptionally(ex -> {
                                throw new RuntimeException("Exception occurred while firing event!", ex);
                            })
                );
            } else {
                handler.fire(event);
            }
        }

        if (customManager != null) {
            futures.add(customManager.fireEventAsync(event, executionOrder)
                    .thenApply(ignored -> null));
        } else if (this != defaultEventManager && defaultEventManager != null) {
            futures.add(defaultEventManager.fireEventAsync(event, executionOrder)
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
        cache.clear();
    }

    public void drop() {
        handlers.clear();
        cache.clear();
    }

    public void setCustomManager(@Nullable EventManager parent) {
        if (this != defaultEventManager) {
            this.customManager = parent;
        }
    }

    public boolean isRegistered(@NotNull EventHandler<?> eventHandler) {
        return handlers.entrySet().stream()
                .anyMatch(entry -> entry.getValue().contains(eventHandler));
    }

    @SuppressWarnings("unchecked")
    public void cloneEventManager(@NotNull EventManager originalEventManager) {
        originalEventManager.handlers.forEach((key, value) -> value.forEach(entry1 -> register((Class<Event>) key, (EventHandler<Event>) entry1)));
    }

    public void destroy() {
        handlers.clear();
        cache.clear();

        if (this == defaultEventManager && executor != null) {
            ExecutorProvider.destroyExecutor(executor);
        }
    }

    private @NotNull List<@NotNull EventHandler<? extends Event>> resolveHandlers(@NotNull Class<?> eventClass, @NotNull EventExecutionOrder order) {
        var byOrder = cache.computeIfAbsent(eventClass, cls -> {
            EnumMap<EventExecutionOrder, List<EventHandler<? extends Event>>> map = new EnumMap<>(EventExecutionOrder.class);

            for (var o : EventExecutionOrder.VALUES) {
                map.put(o, new ArrayList<>());
            }

            for (var entry : handlers.entrySet()) {
                if (entry.getKey().isAssignableFrom(cls)) {
                    for (var handler : entry.getValue()) {
                        map.get(handler.getExecutionOrder()).add(handler);
                    }
                }
            }

            return map;
        });

        return byOrder.get(order);
    }

    public abstract boolean isServerThread();
}
