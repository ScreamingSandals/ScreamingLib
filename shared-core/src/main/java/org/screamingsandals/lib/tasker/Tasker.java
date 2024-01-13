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

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.tasker.task.TaskBase;
import org.screamingsandals.lib.tasker.task.Task;
import org.screamingsandals.lib.utils.Pair;
import org.screamingsandals.lib.utils.annotations.ProvidedService;

import java.util.function.Consumer;

@ProvidedService
public abstract class Tasker {
    private static @Nullable Tasker instance;

    @ApiStatus.Internal
    protected Tasker() {
        if (instance != null) {
            throw new UnsupportedOperationException("Tasker is already initialized!");
        }

        instance = this;
    }

    /**
     * Runs a new task as soon as possible. For async threads, this is immediate; for sync threads, this is going to be executed the next tick.
     *
     * @param property a property owned by a thread the task should run on
     * @param runnable the runnable to run
     * @return new task
     */
    public static @NotNull Task run(@NotNull ThreadProperty property, @NotNull Runnable runnable) {
        if (instance == null) {
            throw new UnsupportedOperationException("Tasker is not initialized yet!");
        }
        return instance.run0(property, runnable);
    }

    /**
     * Runs a new task as soon as possible in an asynchronous thread.
     *
     * @param runnable the runnable to run
     * @return new task
     */
    public static @NotNull Task runAsync(@NotNull Runnable runnable) {
        return run(DefaultThreads.ASYNC_THREAD, runnable);
    }

    /**
     * Runs a new task after the specified delay.
     *
     * @param property a property owned by a thread the task should run on
     * @param runnable the runnable to run
     * @param delay the time to wait before running the task
     * @param delayUnit the unit in which delay is specified
     * @return new task
     */
    public static @NotNull Task runDelayed(@NotNull ThreadProperty property, @NotNull Runnable runnable, long delay, @NotNull TaskerTime delayUnit) {
        if (instance == null) {
            throw new UnsupportedOperationException("Tasker is not initialized yet!");
        }
        return instance.runDelayed0(property, runnable, delay, delayUnit);
    }

    /**
     * Runs a new task after the specified delay.
     *
     * @param property a property owned by a thread the task should run on
     * @param runnable the runnable to run
     * @param delay the time to wait before running the task
     * @return new task
     */
    public static @NotNull Task runDelayed(@NotNull ThreadProperty property, @NotNull Runnable runnable, @NotNull Pair<@NotNull Long, @NotNull TaskerTime> delay) {
        return runDelayed(property, runnable, delay.first(), delay.second());
    }

    /**
     * Runs a new task after the specified delay in an asynchronous thread.
     *
     * @param runnable the runnable to run
     * @param delay the time to wait before running the task
     * @param delayUnit the unit in which delay is specified
     * @return new task
     */
    public static @NotNull Task runAsyncDelayed(@NotNull Runnable runnable, long delay, @NotNull TaskerTime delayUnit) {
        return runDelayed(DefaultThreads.ASYNC_THREAD, runnable, delay, delayUnit);
    }

    /**
     * Runs a new task after the specified delay in an asynchronous thread.
     *
     * @param runnable the runnable to run
     * @param delay the time to wait before running the task
     * @return new task
     */
    public static @NotNull Task runAsyncDelayed(@NotNull Runnable runnable, @NotNull Pair<@NotNull Long, @NotNull TaskerTime> delay) {
        return runDelayed(DefaultThreads.ASYNC_THREAD, runnable, delay.first(), delay.second());
    }

    /**
     * Creates a new task that will repeatedly run until cancelled.
     *
     * @param property a property owned by a thread the task should run on
     * @param runnable the runnable to run
     * @param period the time to wait between individual runs of the task
     * @param periodUnit the unit in which period is specified
     * @return new task
     */
    public static @NotNull Task runRepeatedly(@NotNull ThreadProperty property, @NotNull Runnable runnable, long period, @NotNull TaskerTime periodUnit) {
        if (instance == null) {
            throw new UnsupportedOperationException("Tasker is not initialized yet!");
        }
        return instance.runRepeatedly0(property, runnable, period, periodUnit);
    }

    /**
     * Creates a new task that will repeatedly run until cancelled.
     *
     * @param property a property owned by a thread the task should run on
     * @param selfCancellable the runnable to run
     * @param period the time to wait between individual runs of the task
     * @param periodUnit the unit in which period is specified
     * @return new task
     */
    public static @NotNull Task runRepeatedly(@NotNull ThreadProperty property, @NotNull Consumer<@NotNull TaskBase> selfCancellable, long period, @NotNull TaskerTime periodUnit) {
        if (instance == null) {
            throw new UnsupportedOperationException("Tasker is not initialized yet!");
        }
        return instance.runRepeatedly0(property, selfCancellable, period, periodUnit);
    }

    /**
     * Creates a new task that will repeatedly run until cancelled.
     *
     * @param property a property owned by a thread the task should run on
     * @param runnable the runnable to run
     * @param period the time to wait between individual runs of the task
     * @return new task
     */
    public static @NotNull Task runRepeatedly(@NotNull ThreadProperty property, @NotNull Runnable runnable, @NotNull Pair<@NotNull Long, @NotNull TaskerTime> period) {
        return runRepeatedly(property, runnable, period.first(), period.second());
    }

    /**
     * Creates a new task that will repeatedly run until cancelled.
     *
     * @param property a property owned by a thread the task should run on
     * @param selfCancellable the runnable to run
     * @param period the time to wait between individual runs of the task
     * @return new task
     */
    public static @NotNull Task runRepeatedly(@NotNull ThreadProperty property, @NotNull Consumer<@NotNull TaskBase> selfCancellable, @NotNull Pair<@NotNull Long, @NotNull TaskerTime> period) {
        return runRepeatedly(property, selfCancellable, period.first(), period.second());
    }

    /**
     * Creates a new task that will repeatedly run in an asynchronous thread until cancelled.
     *
     * @param runnable the runnable to run
     * @param period the time to wait between individual runs of the task
     * @param periodUnit the unit in which period is specified
     * @return new task
     */
    public static @NotNull Task runAsyncRepeatedly(@NotNull Runnable runnable, long period, @NotNull TaskerTime periodUnit) {
        return runRepeatedly(DefaultThreads.ASYNC_THREAD, runnable, period, periodUnit);
    }

    /**
     * Creates a new task that will repeatedly run in an asynchronous thread until cancelled.
     *
     * @param selfCancellable the runnable to run
     * @param period the time to wait between individual runs of the task
     * @param periodUnit the unit in which period is specified
     * @return new task
     */
    public static @NotNull Task runAsyncRepeatedly(@NotNull Consumer<@NotNull TaskBase> selfCancellable, long period, @NotNull TaskerTime periodUnit) {
        return runRepeatedly(DefaultThreads.ASYNC_THREAD, selfCancellable, period, periodUnit);
    }

    /**
     * Creates a new task that will repeatedly run in an asynchronous thread until cancelled.
     *
     * @param runnable the runnable to run
     * @param period the time to wait between individual runs of the task
     * @return new task
     */
    public static @NotNull Task runAsyncRepeatedly(@NotNull Runnable runnable, @NotNull Pair<@NotNull Long, @NotNull TaskerTime> period) {
        return runRepeatedly(DefaultThreads.ASYNC_THREAD, runnable, period.first(), period.second());
    }

    /**
     * Creates a new task that will repeatedly run in an asynchronous thread until cancelled.
     *
     * @param selfCancellable the runnable to run
     * @param period the time to wait between individual runs of the task
     * @return new task
     */
    public static @NotNull Task runAsyncRepeatedly(@NotNull Consumer<@NotNull TaskBase> selfCancellable, @NotNull Pair<@NotNull Long, @NotNull TaskerTime> period) {
        return runRepeatedly(DefaultThreads.ASYNC_THREAD, selfCancellable, period.first(), period.second());
    }

    /**
     * Creates a new task that will repeatedly run until cancelled. First run is going to happen after the specified delay.
     *
     * @param property a property owned by a thread the task should run on
     * @param runnable the runnable to run
     * @param delay the time to wait before running the task
     * @param delayUnit the unit in which delay is specified
     * @param period the time to wait between individual runs of the task
     * @param periodUnit the unit in which period is specified
     * @return new task
     */
    public static @NotNull Task runDelayedAndRepeatedly(@NotNull ThreadProperty property, @NotNull Runnable runnable, long delay, @NotNull TaskerTime delayUnit, long period, @NotNull TaskerTime periodUnit) {
        if (instance == null) {
            throw new UnsupportedOperationException("Tasker is not initialized yet!");
        }
        return instance.runDelayedAndRepeatedly0(property, runnable, delay, delayUnit, period, periodUnit);
    }

    /**
     * Creates a new task that will repeatedly run until cancelled. First run is going to happen after the specified delay.
     *
     * @param property a property owned by a thread the task should run on
     * @param selfCancellable the runnable to run
     * @param delay the time to wait before running the task
     * @param delayUnit the unit in which delay is specified
     * @param period the time to wait between individual runs of the task
     * @param periodUnit the unit in which period is specified
     * @return new task
     */
    public static @NotNull Task runDelayedAndRepeatedly(@NotNull ThreadProperty property, @NotNull Consumer<@NotNull TaskBase> selfCancellable, long delay, @NotNull TaskerTime delayUnit, long period, @NotNull TaskerTime periodUnit) {
        if (instance == null) {
            throw new UnsupportedOperationException("Tasker is not initialized yet!");
        }
        return instance.runDelayedAndRepeatedly0(property, selfCancellable, delay, delayUnit, period, periodUnit);
    }

    /**
     * Creates a new task that will repeatedly run until cancelled. First run is going to happen after the specified delay.
     *
     * @param property a property owned by a thread the task should run on
     * @param runnable the runnable to run
     * @param delay the time to wait before running the task
     * @param period the time to wait between individual runs of the task
     * @return new task
     */
    public static @NotNull Task runDelayedAndRepeatedly(@NotNull ThreadProperty property, @NotNull Runnable runnable, @NotNull Pair<@NotNull Long, @NotNull TaskerTime> delay, @NotNull Pair<@NotNull Long, @NotNull TaskerTime> period) {
        return runDelayedAndRepeatedly(property, runnable, delay.first(), delay.second(), period.first(), period.second());
    }

    /**
     * Creates a new task that will repeatedly run until cancelled. First run is going to happen after the specified delay.
     *
     * @param property a property owned by a thread the task should run on
     * @param selfCancellable the runnable to run
     * @param delay the time to wait before running the task
     * @param period the time to wait between individual runs of the task
     * @return new task
     */
    public static @NotNull Task runDelayedAndRepeatedly(@NotNull ThreadProperty property, @NotNull Consumer<@NotNull TaskBase> selfCancellable, @NotNull Pair<@NotNull Long, @NotNull TaskerTime> delay, @NotNull Pair<@NotNull Long, @NotNull TaskerTime> period) {
        return runDelayedAndRepeatedly(property, selfCancellable, delay.first(), delay.second(), period.first(), period.second());
    }

    /**
     * Creates a new task that will repeatedly run in an asynchronous thread until cancelled. First run is going to happen after the specified delay.
     *
     * @param runnable the runnable to run
     * @param delay the time to wait before running the task
     * @param delayUnit the unit in which delay is specified
     * @param period the time to wait between individual runs of the task
     * @param periodUnit the unit in which period is specified
     * @return new task
     */
    public static @NotNull Task runAsyncDelayedAndRepeatedly(@NotNull Runnable runnable, long delay, @NotNull TaskerTime delayUnit, long period, @NotNull TaskerTime periodUnit) {
        return runDelayedAndRepeatedly(DefaultThreads.ASYNC_THREAD, runnable, delay, delayUnit, period, periodUnit);
    }

    /**
     * Creates a new task that will repeatedly run in an asynchronous thread until cancelled. First run is going to happen after the specified delay.
     *
     * @param selfCancellable the runnable to run
     * @param delay the time to wait before running the task
     * @param delayUnit the unit in which delay is specified
     * @param period the time to wait between individual runs of the task
     * @param periodUnit the unit in which period is specified
     * @return new task
     */
    public static @NotNull Task runAsyncDelayedAndRepeatedly(@NotNull Consumer<@NotNull TaskBase> selfCancellable, long delay, @NotNull TaskerTime delayUnit, long period, @NotNull TaskerTime periodUnit) {
        return runDelayedAndRepeatedly(DefaultThreads.ASYNC_THREAD, selfCancellable, delay, delayUnit, period, periodUnit);
    }

    /**
     * Creates a new task that will repeatedly run in an asynchronous thread until cancelled. First run is going to happen after the specified delay.
     *
     * @param runnable the runnable to run
     * @param delay the time to wait before running the task
     * @param period the time to wait between individual runs of the task
     * @return new task
     */
    public static @NotNull Task runAsyncDelayedAndRepeatedly(@NotNull Runnable runnable, @NotNull Pair<@NotNull Long, @NotNull TaskerTime> delay, @NotNull Pair<@NotNull Long, @NotNull TaskerTime> period) {
        return runDelayedAndRepeatedly(DefaultThreads.ASYNC_THREAD, runnable, delay.first(), delay.second(), period.first(), period.second());
    }

    /**
     * Creates a new task that will repeatedly run in an asynchronous thread until cancelled. First run is going to happen after the specified delay.
     *
     * @param selfCancellable the runnable to run
     * @param delay the time to wait before running the task
     * @param period the time to wait between individual runs of the task
     * @return new task
     */
    public static @NotNull Task runAsyncDelayedAndRepeatedly(@NotNull Consumer<@NotNull TaskBase> selfCancellable, @NotNull Pair<@NotNull Long, @NotNull TaskerTime> delay, @NotNull Pair<@NotNull Long, @NotNull TaskerTime> period) {
        return runDelayedAndRepeatedly(DefaultThreads.ASYNC_THREAD, selfCancellable, delay.first(), delay.second(), period.first(), period.second());
    }

    protected abstract @NotNull Task run0(@NotNull ThreadProperty property, @NotNull Runnable runnable);

    protected abstract @NotNull Task runDelayed0(@NotNull ThreadProperty property, @NotNull Runnable runnable, long delay, @NotNull TaskerTime delayUnit);

    protected abstract @NotNull Task runRepeatedly0(@NotNull ThreadProperty property, @NotNull Runnable runnable, long period, @NotNull TaskerTime periodUnit);

    protected abstract @NotNull Task runRepeatedly0(@NotNull ThreadProperty property, @NotNull Consumer<@NotNull TaskBase> selfCancellable, long period, @NotNull TaskerTime periodUnit);

    protected abstract @NotNull Task runDelayedAndRepeatedly0(@NotNull ThreadProperty property, @NotNull Runnable runnable, long delay, @NotNull TaskerTime delayUnit, long period, @NotNull TaskerTime periodUnit);

    protected abstract @NotNull Task runDelayedAndRepeatedly0(@NotNull ThreadProperty property, @NotNull Consumer<@NotNull TaskBase> selfCancellable, long delay, @NotNull TaskerTime delayUnit, long period, @NotNull TaskerTime periodUnit);
}
