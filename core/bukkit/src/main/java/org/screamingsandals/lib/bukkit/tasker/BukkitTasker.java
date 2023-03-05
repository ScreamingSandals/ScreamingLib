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

package org.screamingsandals.lib.bukkit.tasker;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.bukkit.tasker.task.BukkitTaskerTask;
import org.screamingsandals.lib.tasker.TaskBuilderImpl;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class BukkitTasker extends Tasker {
    private final @NotNull Plugin plugin;
    private final @NotNull BukkitScheduler scheduler;

    public BukkitTasker(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.scheduler = plugin.getServer().getScheduler();
    }

    @Override
    public @NotNull TaskerTask start0(@NotNull TaskBuilderImpl builder) {
        final var runnable = builder.getRunnable();

        if (builder.isAsync()
                && !builder.isAfterOneTick()
                && builder.getRepeat() == 0
                && builder.getDelay() == 0) {
            return new BukkitTaskerTask(builder.getTaskId(), scheduler.runTaskAsynchronously(plugin, runnable));
        }

        if (builder.isAfterOneTick()) {
            return new BukkitTaskerTask(builder.getTaskId(), scheduler.runTask(plugin, runnable));
        }

        final var timeUnit = Preconditions.checkNotNull(builder.getTimeUnit(), "TimeUnit cannot be null!");
        if (builder.getDelay() > 0 && builder.getRepeat() <= 0) {
            if (builder.isAsync()) {
                return new BukkitTaskerTask(
                        builder.getTaskId(),
                        scheduler.runTaskLaterAsynchronously(
                                plugin,
                                runnable,
                                timeUnit.getBukkitTime(builder.getDelay())
                        )
                );
            } else {
                return new BukkitTaskerTask(
                        builder.getTaskId(),
                        scheduler.runTaskLater(
                                plugin,
                                runnable,
                                timeUnit.getBukkitTime(builder.getDelay())
                        )
                );
            }
        }

        if (builder.getRepeat() > 0) {
            if (builder.isAsync()) {
                return new BukkitTaskerTask(
                        builder.getTaskId(),
                        scheduler.runTaskTimerAsynchronously(
                                plugin,
                                runnable,
                                timeUnit.getBukkitTime(builder.getDelay()),
                                timeUnit.getBukkitTime(builder.getRepeat())
                        )
                );
            } else {
                return new BukkitTaskerTask(
                        builder.getTaskId(),
                        scheduler.runTaskTimer(
                                plugin,
                                runnable,
                                timeUnit.getBukkitTime(builder.getDelay()),
                                timeUnit.getBukkitTime(builder.getRepeat())
                        )
                );
            }
        }

        throw new UnsupportedOperationException("Unsupported Tasker state!");
    }
}
