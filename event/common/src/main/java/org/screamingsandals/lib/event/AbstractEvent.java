package org.screamingsandals.lib.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Abstract event class, every event should extend this
 */
@Getter
@RequiredArgsConstructor
public abstract class AbstractEvent {
    /**
     * Indicates if this event is async
     */
    private final boolean async;

    public AbstractEvent() {
        this.async = false;
    }
}
