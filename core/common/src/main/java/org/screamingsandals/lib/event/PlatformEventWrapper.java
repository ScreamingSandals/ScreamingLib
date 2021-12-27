package org.screamingsandals.lib.event;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.Optional;

public interface PlatformEventWrapper extends Wrapper, RawValueHolder {
    @Override
    @ApiStatus.Experimental
    default Object raw() {
        return getEvent();
    }

    @ApiStatus.Experimental
    Object getEvent();

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    @ApiStatus.Experimental
    default <T> T as(Class<T> type) {
        if (type.isInstance(getEvent())) {
            return (T) getEvent();
        }
        throw new UnsupportedOperationException("Can't unwrap wrapper to " + type.getName());
    }

    @Override
    @ApiStatus.Experimental
    default <T> Optional<T> asOptional(Class<T> type) {
        return Wrapper.super.asOptional(type);
    }
}
