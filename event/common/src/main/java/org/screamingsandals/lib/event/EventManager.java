package org.screamingsandals.lib.event;

import lombok.Getter;
import org.screamingsandals.lib.utils.executor.AbstractServiceWithExecutor;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventManager extends AbstractServiceWithExecutor {
    @Getter
    private static final EventManager defaultEventManager = new EventManager();

    @Getter
    private EventManager customManager;
    private final Map<Class<?>, List<EventHandler<? extends AbstractEvent>>> handlers = new HashMap<>();

    public EventManager() {
        super("SSEventManager");
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

    public <T extends AbstractEvent> EventHandler<T> register(Class<T> event, Consumer<T> consumer, boolean ignoreCancelled) {
        return register(event, EventHandler.of(consumer, ignoreCancelled));
    }

    public <T extends AbstractEvent> EventHandler<T> register(Class<T> event, Consumer<T> consumer, EventPriority eventPriority) {
        return register(event, EventHandler.of(consumer, eventPriority));
    }

    public <T extends AbstractEvent> EventHandler<T> register(Class<T> event, Consumer<T> consumer, EventPriority eventPriority,
                                                              boolean ignoreCancelled) {
        return register(event, EventHandler.of(consumer, eventPriority, ignoreCancelled));
    }

    public <T extends AbstractEvent> EventHandler<T> register(Class<T> event, EventHandler<T> handler) {
        if (!handlers.containsKey(event)) {
            handlers.put(event, new ArrayList<>());
        }
        handlers.get(event).add(handler);
        fireEvent(new HandlerRegisteredEvent(this, event, handler));

        return handler;
    }

    public <T extends AbstractEvent> void unregister(EventHandler<T> handler) {
        handlers.forEach((event, consumers) ->
                consumers.removeIf(e -> {
                    if (handler == e) {
                        fireEvent(new HandlerUnregisteredEvent(this, event, handler));
                        return true;
                    }
                    return false;
                }));
    }

    public <K extends AbstractEvent> K fireEvent(K event) {
        EventPriority.VALUES.forEach(priority -> fireEvent(event, priority));
        return event;
    }

    public <K extends AbstractEvent> K fireEvent(K event, EventPriority eventPriority) {
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

    public <K extends AbstractEvent> CompletableFuture<K> fireEventAsync(K event) {
        final var futures = new LinkedList<CompletableFuture<K>>();
        EventPriority.VALUES.forEach(priority -> futures.add(fireEventAsync(event, priority)));

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(ignored -> event);
    }

    public <K extends AbstractEvent> CompletableFuture<K> fireEventAsync(K event, EventPriority eventPriority) {
        if (!event.isAsync()) {
            //we don't want non-async events to be called async. :)
            return CompletableFuture.completedFuture(fireEvent(event, eventPriority));
        }

        final var futures = findEventHandlers(event, eventPriority)
                .map(eventHandler -> CompletableFuture.runAsync(() -> eventHandler.fire(event), executor)
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
        Map.copyOf(handlers)
                .forEach((event, eventHandlers) -> List.copyOf(eventHandlers)
                        .forEach(this::unregister));
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
        return handlers.values().stream().anyMatch(eventHandlers -> eventHandlers.contains(eventHandler));
    }

    private <E extends AbstractEvent> Stream<EventHandler<? extends AbstractEvent>> findEventHandlers(E event, EventPriority priority) {
        return handlers.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isInstance(event))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .filter(eventHandler -> eventHandler.getEventPriority() == priority);
    }

    @SuppressWarnings("unchecked")
    public void cloneEventManager(EventManager originalEventManager) {
        originalEventManager.handlers.forEach((aClass, eventHandlers) ->
            eventHandlers.forEach(eventHandler ->
                register((Class<AbstractEvent>) aClass, (EventHandler<AbstractEvent>) eventHandler)
            )
        );
    }
}
