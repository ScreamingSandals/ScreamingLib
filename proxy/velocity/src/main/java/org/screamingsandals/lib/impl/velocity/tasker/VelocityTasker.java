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

package org.screamingsandals.lib.impl.velocity.tasker;

import com.velocitypowered.api.scheduler.Scheduler;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.velocity.tasker.task.VelocityTask;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.tasker.ThreadProperty;
import org.screamingsandals.lib.tasker.task.TaskBase;
import org.screamingsandals.lib.tasker.task.Task;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.internal.PlatformPluginObject;

import java.util.function.Consumer;

@Service
public class VelocityTasker extends Tasker {
    // only async scheduler is present in Velocity, every ThreadProperty is ignored (only global_thread and async_thread properties should be present anyways)
    private final @NotNull Object owner;
    private final @NotNull Scheduler scheduler;

    public VelocityTasker(@PlatformPluginObject @NotNull Object owner, @NotNull Scheduler scheduler) {
        this.owner = owner;
        this.scheduler = scheduler;
    }

    @Override
    protected @NotNull Task run0(@NotNull ThreadProperty property, @NotNull Runnable runnable) {
        return new VelocityTask(scheduler.buildTask(owner, runnable).schedule());
    }

    @Override
    protected @NotNull Task runDelayed0(@NotNull ThreadProperty property, @NotNull Runnable runnable, long delay, @NotNull TaskerTime delayUnit) {
        return new VelocityTask(
                scheduler
                        .buildTask(owner, runnable)
                        .delay(delayUnit.getTime((int) delay), delayUnit.getTimeUnit())
                        .schedule()
        );
    }

    @Override
    protected @NotNull Task runRepeatedly0(@NotNull ThreadProperty property, @NotNull Runnable runnable, long period, @NotNull TaskerTime periodUnit) {
        return new VelocityTask(
                scheduler
                        .buildTask(owner, runnable)
                        .repeat(periodUnit.getTime((int) period), periodUnit.getTimeUnit())
                        .schedule()
        );
    }

    @Override
    protected @NotNull Task runRepeatedly0(@NotNull ThreadProperty property, @NotNull Consumer<@NotNull TaskBase> selfCancellable, long period, @NotNull TaskerTime periodUnit) {
        return new VelocityTask(
                scheduler
                        .buildTask(owner, (selfTask) -> selfCancellable.accept(selfTask::cancel))
                        .repeat(periodUnit.getTime((int) period), periodUnit.getTimeUnit())
                        .schedule()
        );
    }

    @Override
    protected @NotNull Task runDelayedAndRepeatedly0(@NotNull ThreadProperty property, @NotNull Runnable runnable, long delay, @NotNull TaskerTime delayUnit, long period, @NotNull TaskerTime periodUnit) {
        return new VelocityTask(
                scheduler
                        .buildTask(owner, runnable)
                        .delay(delayUnit.getTime((int) delay), delayUnit.getTimeUnit())
                        .repeat(periodUnit.getTime((int) period), periodUnit.getTimeUnit())
                        .schedule()
        );
    }

    @Override
    protected @NotNull Task runDelayedAndRepeatedly0(@NotNull ThreadProperty property, @NotNull Consumer<@NotNull TaskBase> selfCancellable, long delay, @NotNull TaskerTime delayUnit, long period, @NotNull TaskerTime periodUnit) {
        return new VelocityTask(
                scheduler
                        .buildTask(owner, (selfTask) -> selfCancellable.accept(selfTask::cancel))
                        .delay(delayUnit.getTime((int) delay), delayUnit.getTimeUnit())
                        .repeat(periodUnit.getTime((int) period), periodUnit.getTimeUnit())
                        .schedule()
        );
    }
}
