package org.screamingsandals.lib.tasker;

import lombok.Getter;
import org.screamingsandals.lib.utils.ProtoTaskerTime;
import org.screamingsandals.lib.utils.ProtoWrapper;

import java.util.concurrent.TimeUnit;

public enum TaskerTime implements ProtoWrapper<ProtoTaskerTime> {
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

    @Override
    public ProtoTaskerTime asProto() {
        switch (this) {
            case TICKS:
                return ProtoTaskerTime.TICKS;
            case SECONDS:
                return ProtoTaskerTime.SECONDS;
            case MINUTES:
                return ProtoTaskerTime.MINUTES;
            case HOURS:
                return ProtoTaskerTime.HOURS;
        }
        return ProtoTaskerTime.SECONDS;
    }

    public static TaskerTime from(ProtoTaskerTime wrappedTime) {
        if (wrappedTime == ProtoTaskerTime.TICKS) {
            return TICKS;
        } else if (wrappedTime == ProtoTaskerTime.MINUTES) {
            return MINUTES;
        } else if (wrappedTime == ProtoTaskerTime.HOURS) {
            return HOURS;
        } else {
            return TaskerTime.SECONDS;
        }
    }
}
