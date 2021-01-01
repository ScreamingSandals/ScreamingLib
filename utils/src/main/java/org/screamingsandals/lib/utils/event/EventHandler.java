package org.screamingsandals.lib.utils.event;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.utils.ConsumerExecutor;

import java.util.function.Consumer;

@Data
@RequiredArgsConstructor(staticName = "of")
public class EventHandler<E> {
    private final Consumer<E> consumer;
    private final EventPriority eventPriority;
    private final boolean ignoreCancelled;

    public static <E> EventHandler<E> of(Consumer<E> consumer) {
        return of(consumer, EventPriority.NORMAL, false);
    }

    public static <E> EventHandler<E> of(Consumer<E> consumer, EventPriority eventPriority) {
        return of(consumer, eventPriority, false);
    }

    public static <E> EventHandler<E> of(Consumer<E> consumer, boolean ignoreCancelled) {
        return of(consumer, EventPriority.NORMAL, ignoreCancelled);
    }

    public void fire(E event) {
        try {
            if (ignoreCancelled && event instanceof Cancellable && ((Cancellable) event).isCancelled()) {
                return;
            }
            ConsumerExecutor.execute(consumer, event);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
