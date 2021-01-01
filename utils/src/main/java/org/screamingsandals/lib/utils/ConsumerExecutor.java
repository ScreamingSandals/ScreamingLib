package org.screamingsandals.lib.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * ConsumerExecutor is class used for calling {@link Consumer}, {@link Predicate} and {@link Function}. This class automatically sets delegate for JVM language other than Java.
 */
@NoArgsConstructor(access = AccessLevel.NONE)
public class ConsumerExecutor {

    /**
     * Sets delegate to {@link Consumer} if possible and calls the {@link Consumer}
     *
     * @param consumer Consumer which should be called
     * @param delegate Object that should be consumed
     * @param <D> type of delegate
     * @return the delegate
     */
    public static <D> D execute(Consumer<D> consumer, D delegate) {
        setDelegate(consumer, delegate);
        consumer.accept(delegate);
        return delegate;
    }

    /**
     * Sets delegate to {@link Predicate} if possible and calls the {@link Predicate}
     *
     * @param predicate Predicate which should be called
     * @param delegate Object that should be consumed
     * @param <D> type of delegate
     * @return the result of predicate
     */
    public static <D> boolean execute(Predicate<D> predicate, D delegate) {
        setDelegate(predicate, delegate);
        return predicate.test(delegate);
    }

    /**
     * Sets delegate to {@link Function} if possible and calls the {@link Function}
     *
     * @param function Function which should be called
     * @param delegate Object that should be consumed
     * @param <D> type of delegate
     * @param <R> type of return
     * @return instance of R, non-null safety
     */
    public static <D,R> R execute(Function<D,R> function, D delegate) {
        setDelegate(function, delegate);
        return function.apply(delegate);
    }

    private static void setDelegate(Object closure, Object delegate) {
        try {
            // TODO: test that
            Reflect.getMethod(closure, "setDelegate", Object.class).invoke(delegate);
            Reflect.getMethod(closure, "setResolveStrategy", int.class).invoke(3);
        } catch (Throwable ignored) {}
    }
}
