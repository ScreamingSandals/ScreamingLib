package org.screamingsandals.lib.event;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.executor.ExecutorProvider;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Custom event manager that has it's own {@link java.util.concurrent.ExecutorService}.
 *
 * Always call {@link EventManager#destroy()} when shutting down!
 */
@Service
@NoArgsConstructor
public class EventManager {
    private final static ExecutorService executor;
    private final Multimap<Class<?>, EventHandler<? extends AbstractEvent>> handlers = Multimaps.synchronizedListMultimap(ArrayListMultimap.create());

    @Getter
    private static EventManager defaultEventManager;
    @Getter
    private EventManager customManager;

    static {
        executor = ExecutorProvider.buildExecutor("SEventManager");
    }

    public static void init(Controllable controllable) {
        if (defaultEventManager != null) {
            throw new UnsupportedOperationException("Default EventManager has been already initialized!");
        }

        defaultEventManager = new EventManager(controllable);
    }

    public EventManager(Controllable controllable) {
        controllable.preDisable(this::destroy);
    }

    public static <K extends AbstractEvent> K fire(K event) {
        return defaultEventManager.fireEvent(event);
    }

    public static <K extends AbstractEvent> CompletableFuture<K> fireAsync(K event) {
        return defaultEventManager.fireEventAsync(event);
    }

    public <T extends AbstractEvent> EventHandler<T> register(Class<T> event, Consumer<T> consumer) {
        return register(event, EventHandler.of(consumer));
    }

    public <T extends AbstractEvent> EventHandler<T> registerOneTime(Class<T> event, Function<T, Boolean> function) {
        return register(event, EventHandler.ofOneTime(handler -> e -> {
            if (function.apply(e)) {
                unregister(handler);
            }
        }));
    }

    public <T extends AbstractEvent> EventHandler<T> register(Class<T> event, Consumer<T> consumer, boolean ignoreCancelled) {
        return register(event, EventHandler.of(consumer, ignoreCancelled));
    }

    public <T extends AbstractEvent> EventHandler<T> registerOneTime(Class<T> event, Function<T, Boolean> function, boolean ignoreCancelled) {
        return register(event, EventHandler.ofOneTime(handler -> e -> {
            if (function.apply(e)) {
                unregister(handler);
            }
        }, ignoreCancelled));
    }

    public <T extends AbstractEvent> EventHandler<T> register(Class<T> event, Consumer<T> consumer, EventPriority eventPriority) {
        return register(event, EventHandler.of(consumer, eventPriority));
    }

    public <T extends AbstractEvent> EventHandler<T> registerOneTime(Class<T> event, Function<T, Boolean> function, EventPriority eventPriority,
                                                              boolean ignoreCancelled) {
        return register(event, EventHandler.ofOneTime(handler -> e -> {
            if (function.apply(e)) {
                unregister(handler);
            }
        }, eventPriority, ignoreCancelled));
    }

    public <T extends AbstractEvent> EventHandler<T> register(Class<T> event, Consumer<T> consumer, EventPriority eventPriority,
                                                              boolean ignoreCancelled) {
        return register(event, EventHandler.of(consumer, eventPriority, ignoreCancelled));
    }

    public <T extends AbstractEvent> EventHandler<T> register(Class<T> event, EventHandler<T> handler) {
        handlers.put(event, handler);
        fireEvent(new HandlerRegisteredEvent(this, event, handler));

        return handler;
    }

    public <T extends AbstractEvent> void unregister(EventHandler<T> handler) {
        List.copyOf(handlers.entries())
                .forEach(entry -> {
                    if (handler == entry.getValue()) {
                        fireEvent(new HandlerUnregisteredEvent(this, entry.getKey(), handler));
                        handlers.remove(entry.getKey(), entry.getValue());
                    }
                });
    }

    public <K extends AbstractEvent> K fireEvent(@NotNull K event) {
        EventPriority.VALUES.forEach(priority -> fireEvent(event, priority));
        return event;
    }

    public <K extends AbstractEvent> K fireEvent(@NotNull K event, @NotNull EventPriority eventPriority) {
        if (event instanceof AbstractAsyncEvent) {
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

    public <K extends AbstractEvent> CompletableFuture<K> fireEventAsync(@NotNull K event) {
        final var futures = new LinkedList<CompletableFuture<K>>();
        EventPriority.VALUES.forEach(priority -> futures.add(fireEventAsync(event, priority)));

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(ignored -> event);
    }

    public <K extends AbstractEvent> CompletableFuture<K> fireEventAsync(@NotNull K event, @NotNull EventPriority eventPriority) {
        if (!event.isAsync()) {
            //we don't want non-async events to be called async. :)
            return CompletableFuture.completedFuture(fireEvent(event, eventPriority));
        }

        final var futures = findEventHandlers(event, eventPriority)
                .map(eventHandler ->
                        CompletableFuture.runAsync(() -> eventHandler.fire(event), executor)
                                .exceptionally(ex -> {
                                    throw new RuntimeException("Exception occurred while firing event!", ex);
                                }))
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
        List.copyOf(handlers.values()).forEach(this::unregister);
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
        return handlers.values()
                .contains(eventHandler);
    }

    @SuppressWarnings("unchecked")
    public void cloneEventManager(EventManager originalEventManager) {
        originalEventManager.handlers.entries()
                .forEach(entry ->
                        register((Class<AbstractEvent>) entry.getKey(), (EventHandler<AbstractEvent>) entry.getValue()));
    }

    public void destroy() {
        handlers.clear();

        if (this == defaultEventManager) {
            ExecutorProvider.destroyExecutor(executor);
        }
    }

    private <E extends AbstractEvent> Stream<? extends EventHandler<? extends AbstractEvent>> findEventHandlers(E event, EventPriority priority) {
        return handlers.entries()
                .stream()
                .filter(entry -> entry.getKey().isInstance(event))
                .map(Map.Entry::getValue)
                .filter(eventHandler -> eventHandler.getEventPriority() == priority);
    }

    public static boolean isDefaultInitialized() {
        return defaultEventManager != null;
    }
}
