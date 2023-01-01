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

package org.screamingsandals.lib.bungee.tasker;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import org.screamingsandals.lib.tasker.TaskBuilderImpl;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.initializer.AbstractTaskInitializer;
import org.screamingsandals.lib.tasker.task.AbstractTaskerTask;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class BungeeTaskInitializer extends AbstractTaskInitializer {
    private final Plugin plugin;
    private final TaskScheduler scheduler;

    public static void init(Controllable controllable, Plugin plugin) {
        Tasker.init(() -> new BungeeTaskInitializer(controllable, plugin));
    }

    public BungeeTaskInitializer(Controllable controllable, Plugin plugin) {
        super(controllable);
        this.plugin = plugin;
        this.scheduler = plugin.getProxy().getScheduler();
    }

    @Override
    public TaskerTask start(TaskBuilderImpl builder) {
        final var runnable = builder.getRunnable();
        if (builder.isAfterOneTick()) {
            return AbstractTaskerTask.of(builder.getTaskId(), scheduler.runAsync(plugin, runnable), builder.getStopEvent());
        }

        if (builder.isAsync()
                && builder.getRepeat() == 0
                && builder.getDelay() == 0) {
            return AbstractTaskerTask.of(builder.getTaskId(), scheduler.runAsync(plugin, runnable), builder.getStopEvent());
        }

        final var timeUnit = Preconditions.checkNotNull(builder.getTimeUnit(), "TimeUnit cannot be null!");
        if (builder.getDelay() > 0 && builder.getRepeat() <= 0) {
            return AbstractTaskerTask.of(builder.getTaskId(), scheduler.schedule(plugin, runnable,
                    builder.getTimeUnit().getTime((int) builder.getDelay()),
                    builder.getTimeUnit().getTimeUnit()), builder.getStopEvent());
        }

        if (builder.getRepeat() > 0) {
            return AbstractTaskerTask.of(builder.getTaskId(), scheduler.schedule(plugin, runnable,
                    builder.getTimeUnit().getTime((int) builder.getDelay()),
                    builder.getTimeUnit().getTime((int) builder.getRepeat()),
                    builder.getTimeUnit().getTimeUnit()), builder.getStopEvent());
        }

        throw new UnsupportedOperationException("Unsupported Tasker state!");
    }

    @Override
    public TaskState getState(TaskerTask taskerTask) {
        //TODO: check task
        if (Tasker.getRunningTasks().containsKey(taskerTask.getId())) {
            return TaskState.RUNNING;
        }
        return TaskState.FINISHED;
    }

    @Override
    public void cancel(TaskerTask task) {
        final ScheduledTask toCancel = task.getTaskObject();
        toCancel.cancel();
    }
}
