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

package org.screamingsandals.lib.impl.bukkit.tasker;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.bukkit.tasker.task.BukkitTask;
import org.screamingsandals.lib.tasker.DefaultThreads;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.tasker.ThreadProperty;
import org.screamingsandals.lib.tasker.task.TaskBase;
import org.screamingsandals.lib.tasker.task.Task;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.function.Consumer;

@Service
public class BukkitTasker extends AbstractBukkitTasker {
    private final @NotNull Plugin plugin;
    private final @NotNull BukkitScheduler scheduler;

    public BukkitTasker(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.scheduler = plugin.getServer().getScheduler();
    }

    @Override
    protected @NotNull Task run0(@NotNull ThreadProperty property, @NotNull Runnable runnable) {
        if (isAsync(property)) {
            return new BukkitTask(scheduler.runTaskAsynchronously(plugin, runnable));
        } else {
            return new BukkitTask(scheduler.runTask(plugin, runnable));
        }
    }

    @Override
    protected @NotNull Task runDelayed0(@NotNull ThreadProperty property, @NotNull Runnable runnable, long delay, @NotNull TaskerTime delayUnit) {
        if (isAsync(property)) {
            return new BukkitTask(scheduler.runTaskLaterAsynchronously(plugin, runnable, delayUnit.getBukkitTime(delay)));
        } else {
            return new BukkitTask(scheduler.runTaskLater(plugin, runnable, delayUnit.getBukkitTime(delay)));
        }
    }

    @Override
    protected @NotNull Task runRepeatedly0(@NotNull ThreadProperty property, @NotNull Runnable runnable, long period, @NotNull TaskerTime periodUnit) {
        return runDelayedAndRepeatedly0(property, runnable, 0L, TaskerTime.TICKS, period, periodUnit);
    }

    @Override
    protected @NotNull Task runRepeatedly0(@NotNull ThreadProperty property, @NotNull Consumer<@NotNull TaskBase> selfCancellable, long period, @NotNull TaskerTime periodUnit) {
        return runDelayedAndRepeatedly0(property, selfCancellable, 0L, TaskerTime.TICKS, period, periodUnit);
    }

    @Override
    protected @NotNull Task runDelayedAndRepeatedly0(@NotNull ThreadProperty property, @NotNull Runnable runnable, long delay, @NotNull TaskerTime delayUnit, long period, @NotNull TaskerTime periodUnit) {
        if (isAsync(property)) {
            return new BukkitTask(scheduler.runTaskTimerAsynchronously(plugin, runnable, delayUnit.getBukkitTime(delay), periodUnit.getBukkitTime(period)));
        } else {
            return new BukkitTask(scheduler.runTaskTimer(plugin, runnable, delayUnit.getBukkitTime(delay), periodUnit.getBukkitTime(period)));
        }
    }

    @Override
    protected @NotNull Task runDelayedAndRepeatedly0(@NotNull ThreadProperty property, @NotNull Consumer<@NotNull TaskBase> selfCancellable, long delay, @NotNull TaskerTime delayUnit, long period, @NotNull TaskerTime periodUnit) {
        var runnable = new BukkitRunnable() { // can't use runTaskTimer(Plugin, Consumer<BukkitTask>, ...), it is too new
            @Override
            public void run() {
                selfCancellable.accept(this::cancel);
            }
        };
        if (isAsync(property)) {
            return new BukkitTask(runnable.runTaskTimerAsynchronously(plugin, delayUnit.getBukkitTime(delay), periodUnit.getBukkitTime(period)));
        } else {
            return new BukkitTask(runnable.runTaskTimer(plugin, delayUnit.getBukkitTime(delay), periodUnit.getBukkitTime(period)));
        }
    }

    private boolean isAsync(@NotNull ThreadProperty property) {
        return property == DefaultThreads.ASYNC_THREAD; // everything else (entities, blocks, ...) is considered global thread on Bukkit
    }
}
