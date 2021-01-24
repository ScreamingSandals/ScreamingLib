package org.screamingsandals.lib.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class AbstractEvent {
    private final boolean async;

    public AbstractEvent() {
        this.async = false;
    }
}
