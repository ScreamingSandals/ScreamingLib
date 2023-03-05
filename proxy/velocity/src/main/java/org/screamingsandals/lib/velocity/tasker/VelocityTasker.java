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

import com.velocitypowered.api.scheduler.Scheduler;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.tasker.TaskBuilderImpl;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.velocity.tasker.task.VelocityTaskerTask;

@Service
public class VelocityTasker extends Tasker {
    private final @NotNull Object owner;
    private final @NotNull Scheduler scheduler;

    public VelocityTasker(@NotNull Object owner, @NotNull Scheduler scheduler) {
        this.owner = owner;
        this.scheduler = scheduler;
    }

    @Override
    public @NotNull TaskerTask start0(@NotNull TaskBuilderImpl builder) {
        final var runnable = builder.getRunnable();

        if (builder.isAfterOneTick()) {
            final var taskBuilder = scheduler.buildTask(owner, runnable);
            taskBuilder.delay(TaskerTime.TICKS.getTime(1), TaskerTime.TICKS.getTimeUnit());
            return new VelocityTaskerTask(builder.getTaskId(), taskBuilder.schedule());
        }

        if (builder.isAsync()
                && builder.getRepeat() == 0
                && builder.getDelay() == 0) {
            return new VelocityTaskerTask(builder.getTaskId(), scheduler.buildTask(owner, runnable).schedule());
        }

        final var timeUnit = Preconditions.checkNotNull(builder.getTimeUnit(), "TimeUnit cannot be null!");
        if (builder.getDelay() > 0 && builder.getRepeat() <= 0) {
            return new VelocityTaskerTask(builder.getTaskId(), scheduler.buildTask(owner, runnable)
                    .delay(timeUnit.getTime((int) builder.getRepeat()), timeUnit.getTimeUnit())
                    .schedule());
        }

        if (builder.getRepeat() > 0) {
            return new VelocityTaskerTask(builder.getTaskId(), scheduler.buildTask(owner, runnable)
                    .delay(timeUnit.getTime((int) builder.getDelay()), timeUnit.getTimeUnit())
                    .repeat(timeUnit.getTime((int) builder.getRepeat()), timeUnit.getTimeUnit())
                    .schedule());
        }

        throw new UnsupportedOperationException("Unsupported Tasker state!");
    }
}
