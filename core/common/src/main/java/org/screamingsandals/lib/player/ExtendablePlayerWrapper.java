package org.screamingsandals.lib.player;

import lombok.experimental.Delegate;
import org.screamingsandals.lib.utils.Wrapper;

public class ExtendablePlayerWrapper implements PlayerWrapper {
    @Delegate(types = PlayerWrapper.class, excludes = Wrapper.class)
    private final PlayerWrapper wrappedObject;

    protected ExtendablePlayerWrapper(PlayerWrapper wrappedObject) {
        if (wrappedObject instanceof ExtendablePlayerWrapper) {
            throw new UnsupportedOperationException("ExtendablePlayerWrapper can't wrap another ExtendablePlayerWrapper!");
        }
        this.wrappedObject = wrappedObject;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (type.isInstance(wrappedObject)) {
            return (T) wrappedObject;
        }
        if (type.isInstance(this)) {
            return (T) this;
        }
        return wrappedObject.as(type);
    }
}
