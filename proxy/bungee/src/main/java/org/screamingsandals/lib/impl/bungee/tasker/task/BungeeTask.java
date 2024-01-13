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

package org.screamingsandals.lib.impl.bungee.tasker.task;

import net.md_5.bungee.api.scheduler.ScheduledTask;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.Task;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.concurrent.atomic.AtomicBoolean;

public class BungeeTask extends BasicWrapper<ScheduledTask> implements Task {
    public BungeeTask(@NotNull ScheduledTask wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull TaskState getState() {
        var running = Reflect.fastInvoke(this, "getRunning");
        if (running instanceof AtomicBoolean) {
            if (((AtomicBoolean) running).get()) {
                return TaskState.RUNNING;
            }
        }
        return TaskState.FINISHED;
    }

    @Override
    public void cancel() {
        try {
            wrappedObject.cancel();
        } catch (Exception e) {
            throw new UnsupportedOperationException("Exception while cancelling task " + wrappedObject + "!", e);
        }
    }
}
