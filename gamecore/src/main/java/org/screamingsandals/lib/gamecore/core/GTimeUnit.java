package org.screamingsandals.lib.gamecore.core;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

public enum GTimeUnit {
    TICKS(50, TimeUnit.MILLISECONDS),
    SECONDS(1, TimeUnit.SECONDS),
    MINUTES(1, TimeUnit.MINUTES);

    @Getter
    private final int value;
    @Getter
    private final TimeUnit timeUnit;

    GTimeUnit(int value, TimeUnit timeUnit) {
        this.value = value;
        this.timeUnit = timeUnit;
    }

    public static int getTimeUnitValue(int multiplier, GTimeUnit gTimeUnit) {
        return gTimeUnit.value * multiplier;
    }
}
