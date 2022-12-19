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
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Basic Wrapper is class used for directly wrapping objects without using Mapping classes and Bidirectional Converters.
 *
 * @see BasicNullableWrapper#wrap(Object)
 * @param <O> type of wrapped object
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BasicNullableWrapper<O> implements Wrapper {
    protected final transient @Nullable O wrappedObject;

    public static <O> @NotNull BasicNullableWrapper<O> wrap(@Nullable O wrappedObject) {
        return new BasicNullableWrapper<>(wrappedObject);
    }

    public @Nullable Object raw() {
        return wrappedObject;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type.isInstance(wrappedObject)) {
            return (T) wrappedObject;
        }
        if (type.isInstance(this)) {
            return (T) this;
        }
        throw new UnsupportedOperationException("Can't unwrap object to " + type.getName());
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof RawValueHolder) {
            obj = ((RawValueHolder) obj).raw();
        }
        return Objects.equals(wrappedObject, obj);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(wrappedObject);
    }

    public boolean isPresent() {
        return wrappedObject != null;
    }

    public boolean isEmpty() {
        return wrappedObject == null;
    }

    @Override
    public @NotNull String toString() {
        if (wrappedObject == null) {
            return this.getClass().getSimpleName() + "(null)";
        } else {
            return this.getClass().getSimpleName() + "(" + wrappedObject + ")";
        }
    }
}
