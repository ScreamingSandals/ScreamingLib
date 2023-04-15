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

package org.screamingsandals.lib.impl.bukkit.tasker.task;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.Task;
import org.screamingsandals.lib.utils.BasicWrapper;

public class BukkitTask extends BasicWrapper<org.bukkit.scheduler.BukkitTask> implements Task {
    public BukkitTask(org.bukkit.scheduler.@NotNull BukkitTask wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public void cancel() {
        try {
            wrappedObject.cancel();
        } catch (Exception e) {
            throw new UnsupportedOperationException("Exception while cancelling task " + wrappedObject.getTaskId() + "!", e);
        }
    }

    @Override
    public @NotNull TaskState getState() {
        if (wrappedObject.isCancelled()) {
            return TaskState.FINISHED;
        }

        return TaskState.RUNNING;
    }
}