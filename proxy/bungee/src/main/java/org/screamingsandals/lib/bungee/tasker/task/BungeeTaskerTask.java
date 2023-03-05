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

package org.screamingsandals.lib.bungee.tasker.task;

import lombok.Data;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.TaskerTask;

@Data
public class BungeeTaskerTask implements TaskerTask {
    private final @NotNull Integer id;
    private final @NotNull ScheduledTask taskObject;

    @Override
    public @NotNull TaskState getState() {
        //TODO: check task
        if (Tasker.getRunningTasks().containsKey(id)) {
            return TaskState.RUNNING;
        }
        return TaskState.FINISHED;
    }

    @Override
    public void cancel() {
        try {
            taskObject.cancel();
        } catch (Exception e) {
            throw new UnsupportedOperationException("Exception while cancelling task " + id + "!", e);
        }

        Tasker.unregister(this);
    }
}
