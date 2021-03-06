package org.screamingsandals.lib.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

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

        public static List<Result> VALUES = Arrays.asList(values());

        public static Result convert(String result) {
            return VALUES.stream()
                    .filter(next -> result.equalsIgnoreCase(next.name()))
                    .findFirst()
                    .orElse(Result.ALLOW);
        }
    }
}
