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

    private static void setDelegate(Object callback, Object delegate) {
        Reflect.asInvocationHandler(callback).ifPresent(invocationHandler -> {
            var closure = Reflect.fastInvoke(invocationHandler, "getDelegate");
            if (closure != null) {
                Reflect.getMethod(closure, "setDelegate", Object.class).invoke( delegate);
                Reflect.getMethod(closure, "setResolveStrategy", int.class).invoke(
                        Reflect.getField(closure, "DELEGATE_ONLY")
                );
            }
        });
    }
}
