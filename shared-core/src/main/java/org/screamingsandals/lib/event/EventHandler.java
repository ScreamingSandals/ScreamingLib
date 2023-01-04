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

package org.screamingsandals.lib.event;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;
import org.screamingsandals.lib.utils.ReceiverConsumer;

import java.util.function.Function;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, staticName = "of")
@AllArgsConstructor(staticName = "of")
public final class EventHandler<E extends SEvent> {
    @Setter(AccessLevel.NONE)
    @UnknownNullability("At the end of execution of all of methods, this attribute is not null. However, immediately after the construction, it can be null.")
    private ReceiverConsumer<@NotNull E> consumer;
    private final @NotNull EventPriority eventPriority;
    private final boolean ignoreCancelled;

    public static <E extends SEvent> @NotNull EventHandler<E> of(@NotNull ReceiverConsumer<@NotNull E> consumer) {
        return of(consumer, EventPriority.NORMAL, false);
    }

    public static <E extends SEvent> @NotNull EventHandler<E> of(@NotNull ReceiverConsumer<@NotNull E> consumer, @NotNull EventPriority eventPriority) {
        return of(consumer, eventPriority, false);
    }

    public static <E extends SEvent> @NotNull EventHandler<E> of(@NotNull ReceiverConsumer<@NotNull E> consumer, boolean ignoreCancelled) {
        return of(consumer, EventPriority.NORMAL, ignoreCancelled);
    }

    public static <E extends SEvent> EventHandler<E> ofOneTime(Function<@NotNull EventHandler<E>, @NotNull ReceiverConsumer<@NotNull E>> function) {
        EventHandler<E> manager = of(EventPriority.NORMAL, false);
        manager.consumer = function.apply(manager);
        return manager;
    }

    public static <E extends SEvent> EventHandler<E> ofOneTime(Function<@NotNull EventHandler<E>, @NotNull ReceiverConsumer<@NotNull E>> function, @NotNull EventPriority eventPriority) {
        EventHandler<E> manager = of(eventPriority, false);
        manager.consumer = function.apply(manager);
        return manager;
    }

    public static <E extends SEvent> EventHandler<E> ofOneTime(Function<@NotNull EventHandler<E>, @NotNull ReceiverConsumer<@NotNull E>> function, boolean ignoreCancelled) {
        EventHandler<E> manager = of(EventPriority.NORMAL, ignoreCancelled);
        manager.consumer = function.apply(manager);
        return manager;
    }

    public static <E extends SEvent> EventHandler<E> ofOneTime(Function<@NotNull EventHandler<E>, @NotNull ReceiverConsumer<@NotNull E>> function, @NotNull EventPriority eventPriority, boolean ignoreCancelled) {
        EventHandler<E> manager = of(eventPriority, ignoreCancelled);
        manager.consumer = function.apply(manager);
        return manager;
    }

    @SuppressWarnings("unchecked")
    public void fire(@NotNull SEvent event) {
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
