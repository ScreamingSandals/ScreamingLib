package org.screamingsandals.lib.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

/**
 * Basic Wrapper is class used for directly wrapping objects without using Mapping classes and Bidirectional Converters.
 * Can be used for example for wrapping players in Material Resolver without having Player Utils.
 *
 * @see BasicWrapper#wrap(Object)
 * @param <O> type of wrapped object
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BasicWrapper<O> implements Wrapper {
    protected transient final O wrappedObject;

    public static <O> BasicWrapper<O> wrap(O wrappedObject) {
        return new BasicWrapper<>(wrappedObject);
    }

    public Object raw() {
        return wrappedObject;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T as(Class<T> type) {
        if (type.isInstance(wrappedObject)) {
            return (T) wrappedObject;
        }
        if (type.isInstance(this)) {
            return (T) this;
        }
        throw new UnsupportedOperationException("Can't unwrap object to " + type.getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BasicWrapper) {
            obj = ((BasicWrapper<?>) obj).raw();
        }
        return Objects.equals(wrappedObject, obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wrappedObject);
    }
}
