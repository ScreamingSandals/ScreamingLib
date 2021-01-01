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
        if (!handlers.containsKey(event)) {
            handlers.put(event, new ArrayList<>());
        }
        var handler = EventHandler.of(consumer);
        handlers.get(event).add(handler);
        return handler;
    }

    public <T> EventHandler<T> register(Class<T> event, EventHandler<T> handler) {
        if (!handlers.containsKey(event)) {
            handlers.put(event, new ArrayList<>());
        }
        handlers.get(event).add(handler);
        return handler;
    }

    public <T> void unregister(EventHandler<T> handler) {
        handlers.forEach((aClass, consumers) -> consumers.removeIf(e -> handler == e));
    }

    public void fireEvent(Object event) {
        //noinspection unchecked
        handlers.entrySet().stream()
                .filter(entry -> entry.getKey().isInstance(event))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(EventHandler::getEventPriority))
                .forEach(eventHandler -> ((EventHandler<Object>) eventHandler).fire(event));

        if (parent != null) {
            parent.fireEvent(event);
        } else if (this != baseEventManager) {
            baseEventManager.fireEvent(event);
        }
    }

    public void setParent(EventManager parent) {
        if (this != baseEventManager) {
            this.parent = parent;
        }
    }

    @SuppressWarnings("unchecked")
    public void cloneEventManager(EventManager newEventManager) {
        newEventManager.handlers.forEach((aClass, handlers) -> handlers.forEach(handler ->
                register((Class<Object>) aClass, (EventHandler<Object>) handler)
        ));
    }
}
