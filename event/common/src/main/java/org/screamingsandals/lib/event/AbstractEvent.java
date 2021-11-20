package org.screamingsandals.lib.event;

import java.util.Arrays;
import java.util.List;

/**
 * Abstract event class
 */
@Deprecated
public abstract class AbstractEvent implements SEvent {

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
