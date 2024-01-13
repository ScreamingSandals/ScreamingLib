/*
 * Copyright 2024 ScreamingSandals
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

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.tasker.task.TaskBase;
import org.screamingsandals.lib.tasker.task.Task;
import org.screamingsandals.lib.utils.Pair;

import java.util.function.Consumer;

/**
 * A tasker which belongs to a specific thread.
 */
@RequiredArgsConstructor
public final class ThreadTasker {
    private final @NotNull ThreadProperty property;

    /**
     * Runs a new task as soon as possible (in the next tick).
     *
     * @param runnable the runnable to run
     * @return new task
     */
    public @NotNull Task run(@NotNull Runnable runnable) {
        return Tasker.run(property, runnable);
    }

    /**
     * Runs a new task after the specified delay.
     *
     * @param runnable the runnable to run
     * @param delay the time to wait before running the task
     * @param delayUnit the unit in which delay is specified
     * @return new task
     */
    public @NotNull Task runDelayed(@NotNull Runnable runnable, long delay, @NotNull TaskerTime delayUnit) {
        return Tasker.runDelayed(property, runnable, delay, delayUnit);
    }

    /**
     * Runs a new task after the specified delay.
     *
     * @param runnable the runnable to run
     * @param delay the time to wait before running the task
     * @return new task
     */
    public @NotNull Task runDelayed(@NotNull Runnable runnable, @NotNull Pair<@NotNull Long, @NotNull TaskerTime> delay) {
        return Tasker.runDelayed(property, runnable, delay);
    }

    /**
     * Creates a new task that will repeatedly run until cancelled.
     *
     * @param runnable the runnable to run
     * @param period the time to wait between individual runs of the task
     * @param periodUnit the unit in which period is specified
     * @return new task
     */
    public @NotNull Task runRepeatedly(@NotNull Runnable runnable, long period, @NotNull TaskerTime periodUnit) {
        return Tasker.runRepeatedly(property, runnable, period, periodUnit);
    }

    /**
     * Creates a new task that will repeatedly run until cancelled.
     *
     * @param selfCancellable the runnable to run
     * @param period the time to wait between individual runs of the task
     * @param periodUnit the unit in which period is specified
     * @return new task
     */
    public @NotNull Task runRepeatedly(@NotNull Consumer<@NotNull TaskBase> selfCancellable, long period, @NotNull TaskerTime periodUnit) {
        return Tasker.runRepeatedly(property, selfCancellable, period, periodUnit);
    }

    /**
     * Creates a new task that will repeatedly run until cancelled.
     *
     * @param runnable the runnable to run
     * @param period the time to wait between individual runs of the task
     * @return new task
     */
    public @NotNull Task runRepeatedly(@NotNull Runnable runnable, @NotNull Pair<@NotNull Long, @NotNull TaskerTime> period) {
        return Tasker.runRepeatedly(property, runnable, period);
    }

    /**
     * Creates a new task that will repeatedly run until cancelled.
     *
     * @param selfCancellable the runnable to run
     * @param period the time to wait between individual runs of the task
     * @return new task
     */
    public @NotNull Task runRepeatedly(@NotNull Consumer<@NotNull TaskBase> selfCancellable, @NotNull Pair<@NotNull Long, @NotNull TaskerTime> period) {
        return Tasker.runRepeatedly(property, selfCancellable, period);
    }

    /**
     * Creates a new task that will repeatedly run until cancelled. First run is going to happen after the specified delay.
     *
     * @param runnable the runnable to run
     * @param delay the time to wait before running the task
     * @param delayUnit the unit in which delay is specified
     * @param period the time to wait between individual runs of the task
     * @param periodUnit the unit in which period is specified
     * @return new task
     */
    public @NotNull Task runDelayedAndRepeatedly(@NotNull Runnable runnable, long delay, @NotNull TaskerTime delayUnit, long period, @NotNull TaskerTime periodUnit) {
        return Tasker.runDelayedAndRepeatedly(property, runnable, delay, delayUnit, period, periodUnit);
    }

    /**
     * Creates a new task that will repeatedly run until cancelled. First run is going to happen after the specified delay.
     *
     * @param selfCancellable the runnable to run
     * @param delay the time to wait before running the task
     * @param delayUnit the unit in which delay is specified
     * @param period the time to wait between individual runs of the task
     * @param periodUnit the unit in which period is specified
     * @return new task
     */
    public @NotNull Task runDelayedAndRepeatedly(@NotNull Consumer<@NotNull TaskBase> selfCancellable, long delay, @NotNull TaskerTime delayUnit, long period, @NotNull TaskerTime periodUnit) {
        return Tasker.runDelayedAndRepeatedly(property, selfCancellable, delay, delayUnit, period, periodUnit);
    }

    /**
     * Creates a new task that will repeatedly run until cancelled. First run is going to happen after the specified delay.
     *
     * @param runnable the runnable to run
     * @param delay the time to wait before running the task
     * @param period the time to wait between individual runs of the task
     * @return new task
     */
    public @NotNull Task runDelayedAndRepeatedly(@NotNull Runnable runnable, @NotNull Pair<@NotNull Long, @NotNull TaskerTime> delay, @NotNull Pair<@NotNull Long, @NotNull TaskerTime> period) {
        return Tasker.runDelayedAndRepeatedly(property, runnable, delay, period);
    }

    /**
     * Creates a new task that will repeatedly run until cancelled. First run is going to happen after the specified delay.
     *
     * @param selfCancellable the runnable to run
     * @param delay the time to wait before running the task
     * @param period the time to wait between individual runs of the task
     * @return new task
     */
    public @NotNull Task runDelayedAndRepeatedly(@NotNull Consumer<@NotNull TaskBase> selfCancellable, @NotNull Pair<@NotNull Long, @NotNull TaskerTime> delay, @NotNull Pair<@NotNull Long, @NotNull TaskerTime> period) {
        return Tasker.runDelayedAndRepeatedly(property, selfCancellable, delay, period);
    }
}
