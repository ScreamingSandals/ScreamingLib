package org.screamingsandals.lib.core.tasker;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

/**
 * Convert normal TimeUnit to tick values for bukkit
 */
public enum TaskerUnit {
    TICKS(1, 50, TimeUnit.MILLISECONDS),
    SECONDS(20, 1, TimeUnit.SECONDS),
    MINUTES(1200, 1, TimeUnit.MINUTES),
    HOURS(72000, 1, TimeUnit.HOURS);

    @Getter
    private final long bukkitValue;
    @Getter
    private final int timeUnitValue;
    @Getter
    private final TimeUnit timeUnit;

    TaskerUnit(long bukkitValue, int timeUnitValue, TimeUnit timeUnit) {
        this.bukkitValue = bukkitValue;
        this.timeUnitValue = timeUnitValue;
        this.timeUnit = timeUnit;
    }

    public long getBukkitTime(int multiplier) {
        return bukkitValue * multiplier;
    }

    public int getTime(int multiplier) {
        return timeUnitValue * multiplier;
    }
}
