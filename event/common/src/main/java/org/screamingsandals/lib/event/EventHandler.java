package org.screamingsandals.lib.event;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.ConsumerExecutor;

import java.util.function.Consumer;
import java.util.function.Function;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, staticName = "of")
@AllArgsConstructor(staticName = "of")
public class EventHandler<E extends AbstractEvent> {
    @Setter(value = AccessLevel.NONE)
    private Consumer<E> consumer;
    private final EventPriority eventPriority;
    private final boolean ignoreCancelled;

    public static <E extends AbstractEvent> EventHandler<E> of(Consumer<E> consumer) {
        return of(consumer, EventPriority.NORMAL, false);
    }

    public static <E extends AbstractEvent> EventHandler<E> of(Consumer<E> consumer, EventPriority eventPriority) {
        return of(consumer, eventPriority, false);
    }

    public static <E extends AbstractEvent> EventHandler<E> of(Consumer<E> consumer, boolean ignoreCancelled) {
        return of(consumer, EventPriority.NORMAL, ignoreCancelled);
    }

    public static <E extends AbstractEvent> EventHandler<E> ofOneTime(Function<@NotNull EventHandler<E>, @NotNull Consumer<E>> function) {
        EventHandler<E> manager = of(EventPriority.NORMAL, false);
        manager.consumer = function.apply(manager);
        return manager;
    }

    public static <E extends AbstractEvent> EventHandler<E> ofOneTime(Function<@NotNull EventHandler<E>, @NotNull Consumer<E>> function, EventPriority eventPriority) {
        EventHandler<E> manager = of(eventPriority, false);
        manager.consumer = function.apply(manager);
        return manager;
    }

    public static <E extends AbstractEvent> EventHandler<E> ofOneTime(Function<@NotNull EventHandler<E>, @NotNull Consumer<E>> function, boolean ignoreCancelled) {
        EventHandler<E> manager = of(EventPriority.NORMAL, ignoreCancelled);
        manager.consumer = function.apply(manager);
        return manager;
    }

    public static <E extends AbstractEvent> EventHandler<E> ofOneTime(Function<@NotNull EventHandler<E>, @NotNull Consumer<E>> function, EventPriority eventPriority, boolean ignoreCancelled) {
        EventHandler<E> manager = of(eventPriority, ignoreCancelled);
        manager.consumer = function.apply(manager);
        return manager;
    }

    @SuppressWarnings("unchecked")
    public void fire(AbstractEvent event) {
        try {
            if (ignoreCancelled
                    && event instanceof Cancellable
                    && ((Cancellable) event).isCancelled()) {
                return;
            }
            ConsumerExecutor.execute(consumer, (E) event);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
