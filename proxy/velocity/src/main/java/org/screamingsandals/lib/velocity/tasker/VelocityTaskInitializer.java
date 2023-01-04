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

package org.screamingsandals.lib.velocity.tasker;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import com.velocitypowered.api.scheduler.Scheduler;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.tasker.TaskBuilderImpl;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.tasker.initializer.AbstractTaskInitializer;
import org.screamingsandals.lib.tasker.task.AbstractTaskerTask;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class VelocityTaskInitializer extends AbstractTaskInitializer {
    private final @NotNull Object owner;
    private final @NotNull Scheduler scheduler;

    public VelocityTaskInitializer(@NotNull Controllable controllable, @NotNull Object owner, @NotNull Scheduler scheduler) {
        super(controllable);
        this.owner = owner;
        this.scheduler = scheduler;
    }

    public static void init(@NotNull Controllable controllable, @NotNull Object owner, @NotNull ProxyServer proxyServer) {
        Tasker.init(() -> new VelocityTaskInitializer(controllable, owner, proxyServer.getScheduler()));
    }

    @Override
    public @NotNull TaskerTask start(@NotNull TaskBuilderImpl builder) {
        final var runnable = builder.getRunnable();

        if (builder.isAfterOneTick()) {
            final var taskBuilder = scheduler.buildTask(owner, runnable);
            taskBuilder.delay(TaskerTime.TICKS.getTime(1), TaskerTime.TICKS.getTimeUnit());
            return AbstractTaskerTask.of(builder.getTaskId(), taskBuilder.schedule(), builder.getStopEvent());
        }

        if (builder.isAsync()
                && builder.getRepeat() == 0
                && builder.getDelay() == 0) {
            return AbstractTaskerTask.of(builder.getTaskId(), scheduler.buildTask(owner, runnable).schedule(), builder.getStopEvent());
        }

        final var timeUnit = Preconditions.checkNotNull(builder.getTimeUnit(), "TimeUnit cannot be null!");
        if (builder.getDelay() > 0 && builder.getRepeat() <= 0) {
            return AbstractTaskerTask.of(builder.getTaskId(), scheduler.buildTask(owner, runnable)
                    .delay(timeUnit.getTime((int) builder.getRepeat()), timeUnit.getTimeUnit())
                    .schedule(), builder.getStopEvent());
        }

        if (builder.getRepeat() > 0) {
            return AbstractTaskerTask.of(builder.getTaskId(), scheduler.buildTask(owner, runnable)
                    .delay(timeUnit.getTime((int) builder.getDelay()), timeUnit.getTimeUnit())
                    .repeat(timeUnit.getTime((int) builder.getRepeat()), timeUnit.getTimeUnit())
                    .schedule(), builder.getStopEvent());
        }

        throw new UnsupportedOperationException("Unsupported Tasker state!");
    }

    @Override
    public @NotNull TaskState getState(@NotNull TaskerTask taskerTask) {
        final ScheduledTask task = taskerTask.getTaskObject();

        switch (task.status()) {
            case CANCELLED:
                return TaskState.CANCELLED;
            case FINISHED:
                return TaskState.FINISHED;
            default:
                return TaskState.SCHEDULED;
        }
    }

    @Override
    public void cancel(@NotNull TaskerTask task) {
        final ScheduledTask toCancel = task.getTaskObject();
        toCancel.cancel();
    }
}
