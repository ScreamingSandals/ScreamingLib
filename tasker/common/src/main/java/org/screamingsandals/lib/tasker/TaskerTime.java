package org.screamingsandals.lib.tasker;

import lombok.Getter;
import org.screamingsandals.lib.utils.WrappedTaskerTime;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public enum TaskerTime {
    TICKS(1, 50, TimeUnit.MILLISECONDS, WrappedTaskerTime.TICKS),
    SECONDS(20, 1, TimeUnit.SECONDS, WrappedTaskerTime.SECONDS),
    MINUTES(1200, 1, TimeUnit.MINUTES, WrappedTaskerTime.MINUTES),
    HOURS(72000, 1, TimeUnit.HOURS, WrappedTaskerTime.HOURS);

    private static final List<TaskerTime> values = Arrays.asList(values());

    @Getter
    private final long bukkitValue;
    @Getter
    private final int timeUnitValue;
    @Getter
    private final TimeUnit timeUnit;
    @Getter
    private final WrappedTaskerTime wrappedTime;

    TaskerTime(long bukkitValue,
               int timeUnitValue,
               TimeUnit timeUnit,
               WrappedTaskerTime wrappedTime) {
        this.bukkitValue = bukkitValue;
        this.timeUnitValue = timeUnitValue;
        this.timeUnit = timeUnit;
        this.wrappedTime = wrappedTime;
    }

    public long getBukkitTime(long multiplier) {
        return bukkitValue * multiplier;
    }

    public int getTime(int multiplier) {
        return timeUnitValue * multiplier;
    }

    public static TaskerTime from(WrappedTaskerTime wrappedTime) {
        return values.stream()
                .filter(next -> next.getWrappedTime() == wrappedTime)
                .findFirst()
                .orElse(TaskerTime.SECONDS);
    }
}
