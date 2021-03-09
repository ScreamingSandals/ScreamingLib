package org.screamingsandals.lib.utils.reflect;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Executable;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ReflectedExecutable<T extends ReflectedExecutable<T>> {
    default boolean isPresent() {
        return get() != null;
    }

    default boolean isEmpty() {
        return get() == null;
    }

    default T orElse(T newOne) {
        if (isEmpty()) {
            return newOne;
        } else {
            return self();
        }
    }

    default T orElseGet(Supplier<T> supplier) {
        if (isEmpty()) {
            return supplier.get();
        } else {
            return self();
        }
    }

    default T orElseThrow() {
        if (isEmpty()) {
            throw new NoSuchElementException("No value present");
        }
        return self();
    }

    default void ifPresent(Consumer<T> consumer) {
        if (isPresent()) {
            consumer.accept(self());
        }
    }

    default void ifPresentOrElse(Consumer<T> consumer, Runnable orElse) {
        if (isPresent()) {
            consumer.accept(self());
        } else {
            orElse.run();
        }
    }

    T self();

    @Nullable
    Executable get();

}
