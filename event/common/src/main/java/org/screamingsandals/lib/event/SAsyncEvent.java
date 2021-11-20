package org.screamingsandals.lib.event;

public interface SAsyncEvent extends SEvent {
    @Override
    default boolean isAsync() {
        return true;
    }
}
