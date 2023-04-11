/*
 * Copyright 2023 ScreamingSandals
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
import org.jetbrains.annotations.NotNull;

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
    private final @NotNull TimeUnit timeUnit;

    TaskerTime(long bukkitValue, int timeUnitValue, @NotNull TimeUnit timeUnit) {
        this.bukkitValue = bukkitValue;
        this.timeUnitValue = timeUnitValue;
        this.timeUnit = timeUnit;
    }

    public long getBukkitTime(long multiplier) {
        return bukkitValue * multiplier;
    }

    public long getTime(long multiplier) {
        return timeUnitValue * multiplier;
    }
}
