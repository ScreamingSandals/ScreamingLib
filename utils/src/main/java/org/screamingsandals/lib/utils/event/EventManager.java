package org.screamingsandals.lib.utils.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.function.Consumer;

@NoArgsConstructor
@AllArgsConstructor
public class EventManager {

    @Getter
    private static final EventManager baseEventManager = new EventManager();

    @Getter
    private EventManager parent;
    private final Map<Class<?>, List<EventHandler<?>>> handlers = new HashMap<>();

    public <T> EventHandler<T> register(Class<T> event, Consumer<T> consumer) {
        return register(event, EventHandler.of(consumer));
    }

    public <T> EventHandler<T> register(Class<T> event, Consumer<T> consumer, boolean ignoreCancelled) {
        return register(event, EventHandler.of(consumer, ignoreCancelled));
    }

    public <T> EventHandler<T> register(Class<T> event, Consumer<T> consumer, EventPriority eventPriority) {
        return register(event, EventHandler.of(consumer, eventPriority));
    }

    public <T> EventHandler<T> register(Class<T> event, Consumer<T> consumer, EventPriority eventPriority, boolean ignoreCancelled) {
        return register(event, EventHandler.of(consumer, eventPriority, ignoreCancelled));
    }

    public <T> EventHandler<T> register(Class<T> event, EventHandler<T> handler) {
        if (!handlers.containsKey(event)) {
            handlers.put(event, new ArrayList<>());
        }
        handlers.get(event).add(handler);
        fireEvent(new HandlerRegisteredEvent(this, event, handler));
        return handler;
    }

    public <T> void unregister(EventHandler<T> handler) {
        handlers.forEach((event, consumers) -> consumers.removeIf(e -> {
            if (handler == e) {
                fireEvent(new HandlerUnregisteredEvent(this, event, handler));
                return true;
            }
            return false;
        }));
    }

    public void fireEvent(Object event) {
        Arrays.stream(EventPriority.values()).forEach(priority -> fireEvent(event, priority));
    }

    public void fireEvent(Object event, EventPriority eventPriority) {
        //noinspection unchecked
        handlers.entrySet().stream()
                .filter(entry -> entry.getKey().isInstance(event))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .filter(eventHandler -> eventHandler.getEventPriority() == eventPriority)
                .forEach(eventHandler -> ((EventHandler<Object>) eventHandler).fire(event));

        if (parent != null) {
            parent.fireEvent(event);
        } else if (this != baseEventManager) {
            baseEventManager.fireEvent(event);
        }
    }

    public void unregisterAll() {
        Map.copyOf(handlers).forEach((event, eventHandlers) -> List.copyOf(eventHandlers).forEach(this::unregister));
    }

    public void drop() {
        handlers.clear();
    }

    public void setParent(EventManager parent) {
        if (this != baseEventManager) {
            this.parent = parent;
        }
    }

    public boolean isRegistered(EventHandler<?> eventHandler) {
        return handlers.values().stream().anyMatch(eventHandlers -> eventHandlers.contains(eventHandler));
    }

    @SuppressWarnings("unchecked")
    public void cloneEventManager(EventManager newEventManager) {
        newEventManager.handlers.forEach((event, handlers) -> handlers.forEach(handler ->
                register((Class<Object>) event, (EventHandler<Object>) handler)
        ));
    }
}
