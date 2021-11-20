package org.screamingsandals.lib.event;

/**
 * Event interface, every event should implement this
 */
public interface SEvent {
    default boolean isAsync() {
        return false;
    }
}
