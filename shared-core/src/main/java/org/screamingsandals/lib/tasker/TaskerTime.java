package org.screamingsandals.lib.tasker;

import lombok.Getter;
import org.screamingsandals.lib.utils.WrappedTaskerTime;

import java.util.concurrent.TimeUnit;

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

    TaskerTime(long bukkitValue,
               int timeUnitValue,
               TimeUnit timeUnit) {
        this.bukkitValue = bukkitValue;
        this.timeUnitValue = timeUnitValue;
        this.timeUnit = timeUnit;
    }

    public long getBukkitTime(long multiplier) {
        return bukkitValue * multiplier;
    }

    public int getTime(int multiplier) {
        return timeUnitValue * multiplier;
    }

    public WrappedTaskerTime getWrappedTime() {
        switch (this) {
            case TICKS:
                return WrappedTaskerTime.TICKS;
            case SECONDS:
                return WrappedTaskerTime.SECONDS;
            case MINUTES:
                return WrappedTaskerTime.MINUTES;
            case HOURS:
                return WrappedTaskerTime.HOURS;
        }
        return WrappedTaskerTime.SECONDS;
    }

    public static TaskerTime from(WrappedTaskerTime wrappedTime) {
        if (wrappedTime == WrappedTaskerTime.TICKS) {
            return TICKS;
        } else if (wrappedTime == WrappedTaskerTime.MINUTES) {
            return MINUTES;
        } else if (wrappedTime == WrappedTaskerTime.HOURS) {
            return HOURS;
        } else {
            return TaskerTime.SECONDS;
        }
    }
}
