package org.screamingsandals.lib.utils;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.utils.annotations.ImplicitReceiver;

import java.util.function.Consumer;

@ImplicitReceiver // Kotlin
@FunctionalInterface
public interface ReceiverConsumer<T> extends Consumer<T> {
    @ApiStatus.Internal
    void accept0(T receiver);

    default void accept(T receiver) {
        ConsumerExecutor.setDelegate(this, receiver); // Groovy
        accept0(receiver);
    }
}
