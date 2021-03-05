package org.screamingsandals.lib.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.utils.ClickType;

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

    public enum Result {
        DENY,
        DEFAULT,
        ALLOW;

        public static Result convert(String result) {
            try {
                return Result.valueOf(result.toUpperCase());
            } catch (IllegalArgumentException ex) {
                return Result.ALLOW;
            }
        }
    }
}
