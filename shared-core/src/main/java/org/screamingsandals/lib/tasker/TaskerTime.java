/*
 * Copyright 2022 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.tasker;

import lombok.Getter;
import org.screamingsandals.lib.utils.ProtoTaskerTime;
import org.screamingsandals.lib.utils.ProtoWrapper;

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
