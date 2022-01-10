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

package org.screamingsandals.lib.utils.reflect;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Executable;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
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

    default <R> R ifPresentOrElseGet(Function<T, R> function, Supplier<R> orElse) {
        if (isPresent()) {
            return function.apply(self());
        } else {
            return orElse.get();
        }
    }

    T self();

    @Nullable
    Executable get();

}
