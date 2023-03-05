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

package org.screamingsandals.lib.bukkit.tasker.task;

import lombok.Data;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.TaskerTask;

@Data
public class BukkitTaskerTask implements TaskerTask {
    private final @NotNull Integer id;
    private final @NotNull BukkitTask taskObject;

    @Override
    public void cancel() {
        try {
            taskObject.cancel();
        } catch (Exception e) {
            throw new UnsupportedOperationException("Exception while cancelling task " + id + "!", e);
        }

        Tasker.unregister(this);
    }

    @Override
    public @NotNull TaskState getState() {
        if (taskObject.isCancelled()) {
            return TaskState.FINISHED;
        }

        return TaskState.RUNNING;
    }
}
