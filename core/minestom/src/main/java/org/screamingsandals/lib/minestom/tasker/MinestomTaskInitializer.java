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

package org.screamingsandals.lib.minestom.tasker;

import org.screamingsandals.lib.utils.Preconditions;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extensions.Extension;
import net.minestom.server.timer.SchedulerManager;
import net.minestom.server.timer.Task;
import net.minestom.server.utils.time.TimeUnit;
import org.screamingsandals.lib.tasker.TaskBuilderImpl;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.tasker.initializer.AbstractTaskInitializer;
import org.screamingsandals.lib.tasker.task.AbstractTaskerTask;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.Service;

import java.time.temporal.TemporalUnit;

@Service
public class MinestomTaskInitializer extends AbstractTaskInitializer {
    private final SchedulerManager scheduler = MinecraftServer.getSchedulerManager();

    public MinestomTaskInitializer(Controllable controllable) {
        super(controllable);
    }

    public static void init(Controllable controllable, Extension plugin) {
        Tasker.init(() -> new MinestomTaskInitializer(controllable));
    }

    @Override
    public TaskerTask start(TaskBuilderImpl builder) {
        final var runnable = builder.getRunnable();
        if (builder.isAfterOneTick()) {
            final var taskBuilder = scheduler.buildTask(runnable);
            taskBuilder.delay(TaskerTime.TICKS.getTime(1), convert(TaskerTime.TICKS));
            return AbstractTaskerTask.of(builder.getTaskId(), taskBuilder.schedule(), builder.getStopEvent());
        }

        if (builder.isAsync()
                && builder.getRepeat() == 0
                && builder.getDelay() == 0) {
            return AbstractTaskerTask.of(builder.getTaskId(), scheduler.buildTask(runnable).schedule(), builder.getStopEvent());
        }

        final TaskerTime timeUnit = Preconditions.checkNotNull(builder.getTimeUnit(), "TimeUnit cannot be null!");
        if (builder.getDelay() > 0 && builder.getRepeat() <= 0) {
            return AbstractTaskerTask.of(builder.getTaskId(), scheduler.buildTask(runnable)
                    .delay(builder.getDelay(), convert(timeUnit))
                    .schedule(), builder.getStopEvent());
        }

        if (builder.getRepeat() > 0) {
            return AbstractTaskerTask.of(builder.getTaskId(), scheduler.buildTask(runnable)
                    .delay(builder.getDelay(), convert(timeUnit))
                    .repeat(builder.getRepeat(), convert(timeUnit))
                    .schedule(), builder.getStopEvent());
        }

        throw new UnsupportedOperationException("Unsupported Tasker state!");
    }

    @Override
    public TaskState getState(TaskerTask taskerTask) {
        final Task task = taskerTask.getTaskObject();
        return convert(task);
    }

    @Override
    public void cancel(TaskerTask task) {
        final Task toCancel = task.getTaskObject();
        toCancel.cancel();
    }

    private TemporalUnit convert(TaskerTime time) {
        switch (time) {
            case SECONDS:
                return TimeUnit.SECOND;
            case MINUTES:
                return TimeUnit.MINUTE;
            case HOURS:
                return TimeUnit.HOUR;
            default:
                return TimeUnit.SERVER_TICK;
        }
    }

    private TaskState convert(Task task) {
        if (task.isAlive()) {
            return TaskState.RUNNING;
        }
        if (task.isParked()) {
            return TaskState.SCHEDULED;
        }
        return TaskState.FINISHED;
    }
}
