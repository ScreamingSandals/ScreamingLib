package org.screamingsandals.lib.core.tasker;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

/**
 * Convert normal TimeUnit to tick values for bukkit
 */
public enum TaskerTime {
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

    TaskerTime(long bukkitValue, int timeUnitValue, TimeUnit timeUnit) {
        this.bukkitValue = bukkitValue;
        this.timeUnitValue = timeUnitValue;
        this.timeUnit = timeUnit;
    }

    public static int getTimeUnitValue(int multiplier, TaskerTime taskerTime) {
        return taskerTime.timeUnitValue * multiplier;
    }

    public static long getBukkitValue(long multiplier, TaskerTime taskerTime) {
        return taskerTime.bukkitValue * multiplier;
    }
}
