package org.screamingsandals.lib.utils.event;

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
