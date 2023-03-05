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
import net.md_5.bungee.api.scheduler.TaskScheduler;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.bungee.tasker.task.BungeeTaskerTask;
import org.screamingsandals.lib.tasker.TaskBuilderImpl;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class BungeeTasker extends Tasker {
    private final @NotNull Plugin plugin;
    private final @NotNull TaskScheduler scheduler;

    public BungeeTasker(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.scheduler = plugin.getProxy().getScheduler();
    }

    @Override
    public @NotNull TaskerTask start0(@NotNull TaskBuilderImpl builder) {
        final var runnable = builder.getRunnable();
        if (builder.isAfterOneTick()) {
            return new BungeeTaskerTask(builder.getTaskId(), scheduler.runAsync(plugin, runnable));
        }

        if (builder.isAsync()
                && builder.getRepeat() == 0
                && builder.getDelay() == 0) {
            return new BungeeTaskerTask(builder.getTaskId(), scheduler.runAsync(plugin, runnable));
        }

        final var timeUnit = Preconditions.checkNotNull(builder.getTimeUnit(), "TimeUnit cannot be null!");
        if (builder.getDelay() > 0 && builder.getRepeat() <= 0) {
            return new BungeeTaskerTask(
                    builder.getTaskId(),
                    scheduler.schedule(
                            plugin,
                            runnable,
                            timeUnit.getTime((int) builder.getDelay()),
                            timeUnit.getTimeUnit()
                    )
            );
        }

        if (builder.getRepeat() > 0) {
            return new BungeeTaskerTask(
                    builder.getTaskId(),
                    scheduler.schedule(
                            plugin,
                            runnable,
                            timeUnit.getTime((int) builder.getDelay()),
                            timeUnit.getTime((int) builder.getRepeat()),
                            timeUnit.getTimeUnit()
                    )
            );
        }

        throw new UnsupportedOperationException("Unsupported Tasker state!");
    }
}
