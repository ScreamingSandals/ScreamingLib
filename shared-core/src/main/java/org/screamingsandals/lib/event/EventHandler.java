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

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.ConsumerExecutor;
import org.screamingsandals.lib.utils.ReceiverConsumer;

import java.util.function.Consumer;
import java.util.function.Function;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, staticName = "of")
@AllArgsConstructor(staticName = "of")
public class EventHandler<E extends SEvent> {
    @Setter(value = AccessLevel.NONE)
    private ReceiverConsumer<E> consumer;
    private final EventPriority eventPriority;
    private final boolean ignoreCancelled;

    public static <E extends SEvent> EventHandler<E> of(ReceiverConsumer<E> consumer) {
        return of(consumer, EventPriority.NORMAL, false);
    }

    public static <E extends SEvent> EventHandler<E> of(ReceiverConsumer<E> consumer, EventPriority eventPriority) {
        return of(consumer, eventPriority, false);
    }

    public static <E extends SEvent> EventHandler<E> of(ReceiverConsumer<E> consumer, boolean ignoreCancelled) {
        return of(consumer, EventPriority.NORMAL, ignoreCancelled);
    }

    public static <E extends SEvent> EventHandler<E> ofOneTime(Function<@NotNull EventHandler<E>, @NotNull ReceiverConsumer<E>> function) {
        EventHandler<E> manager = of(EventPriority.NORMAL, false);
        manager.consumer = function.apply(manager);
        return manager;
    }

    public static <E extends SEvent> EventHandler<E> ofOneTime(Function<@NotNull EventHandler<E>, @NotNull ReceiverConsumer<E>> function, EventPriority eventPriority) {
        EventHandler<E> manager = of(eventPriority, false);
        manager.consumer = function.apply(manager);
        return manager;
    }

    public static <E extends SEvent> EventHandler<E> ofOneTime(Function<@NotNull EventHandler<E>, @NotNull ReceiverConsumer<E>> function, boolean ignoreCancelled) {
        EventHandler<E> manager = of(EventPriority.NORMAL, ignoreCancelled);
        manager.consumer = function.apply(manager);
        return manager;
    }

    public static <E extends SEvent> EventHandler<E> ofOneTime(Function<@NotNull EventHandler<E>, @NotNull ReceiverConsumer<E>> function, EventPriority eventPriority, boolean ignoreCancelled) {
        EventHandler<E> manager = of(eventPriority, ignoreCancelled);
        manager.consumer = function.apply(manager);
        return manager;
    }

    @SuppressWarnings("unchecked")
    public void fire(SEvent event) {
        try {
            if (ignoreCancelled
                    && event instanceof Cancellable
                    && ((Cancellable) event).cancelled()) {
                return;
            }
            consumer.accept((E) event);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
