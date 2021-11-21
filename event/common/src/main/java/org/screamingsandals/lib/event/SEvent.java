package org.screamingsandals.lib.event;

import java.util.Arrays;
import java.util.List;

/**
 * Event interface, every event should implement this
 */
public interface SEvent {
    default boolean isAsync() {
        return false;
    }

    enum Result {
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
