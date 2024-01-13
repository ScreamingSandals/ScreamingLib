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

package org.screamingsandals.lib.impl.bungee.tasker;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.bungee.tasker.task.BungeeTask;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.tasker.ThreadProperty;
import org.screamingsandals.lib.tasker.task.TaskBase;
import org.screamingsandals.lib.tasker.task.Task;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@Service
public class BungeeTasker extends Tasker {
    // only async scheduler is present in Bungee, every ThreadProperty is ignored (only global_thread and async_thread properties should be present anyways)
    private final @NotNull Plugin plugin;
    private final @NotNull TaskScheduler scheduler;

    public BungeeTasker(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.scheduler = plugin.getProxy().getScheduler();
    }

    @Override
    protected @NotNull Task run0(@NotNull ThreadProperty property, @NotNull Runnable runnable) {
        return new BungeeTask(scheduler.runAsync(plugin, runnable));
    }

    @Override
    protected @NotNull Task runDelayed0(@NotNull ThreadProperty property, @NotNull Runnable runnable, long delay, @NotNull TaskerTime delayUnit) {
        return new BungeeTask(scheduler.schedule(plugin, runnable, delayUnit.getTime(delay), delayUnit.getTimeUnit()));
    }

    @Override
    protected @NotNull Task runRepeatedly0(@NotNull ThreadProperty property, @NotNull Runnable runnable, long period, @NotNull TaskerTime periodUnit) {
        return new BungeeTask(scheduler.schedule(plugin, runnable, 0L, periodUnit.getTime(period), periodUnit.getTimeUnit()));
    }

    @Override
    protected @NotNull Task runRepeatedly0(@NotNull ThreadProperty property, @NotNull Consumer<@NotNull TaskBase> selfCancellable, long period, @NotNull TaskerTime periodUnit) {
        var atomic = new AtomicReference<ScheduledTask>(null);
        atomic.set(scheduler.schedule(plugin, () -> {
            while (atomic.get() == null); // hell nah, what should we do?? :cry:
            selfCancellable.accept(atomic.get()::cancel);
        }, 0L, periodUnit.getTime(period), periodUnit.getTimeUnit()));
        return new BungeeTask(atomic.get());
    }

    @Override
    protected @NotNull Task runDelayedAndRepeatedly0(@NotNull ThreadProperty property, @NotNull Runnable runnable, long delay, @NotNull TaskerTime delayUnit, long period, @NotNull TaskerTime periodUnit) {
        var millisDelay = delayUnit.getTimeUnit().toMillis(delayUnit.getTime(delay));
        var millisPeriod = periodUnit.getTimeUnit().toMillis(periodUnit.getTime(period));
        return new BungeeTask(scheduler.schedule(plugin, runnable, millisDelay, millisPeriod, TimeUnit.MILLISECONDS));
    }

    @Override
    protected @NotNull Task runDelayedAndRepeatedly0(@NotNull ThreadProperty property, @NotNull Consumer<@NotNull TaskBase> selfCancellable, long delay, @NotNull TaskerTime delayUnit, long period, @NotNull TaskerTime periodUnit) {
        var millisDelay = delayUnit.getTimeUnit().toMillis(delayUnit.getTime(delay));
        var millisPeriod = periodUnit.getTimeUnit().toMillis(periodUnit.getTime(period));
        var atomic = new AtomicReference<ScheduledTask>(null);
        atomic.set(scheduler.schedule(plugin, () -> {
            while (atomic.get() == null); // hell nah, what should we do?? :cry:
            selfCancellable.accept(atomic.get()::cancel);
        },  millisDelay, millisPeriod, TimeUnit.MILLISECONDS));
        return new BungeeTask(atomic.get());
    }
}
