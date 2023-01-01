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

package org.screamingsandals.lib.tasker;

import org.screamingsandals.lib.tasker.task.TaskerTask;

import java.util.LinkedList;
import java.util.List;

public class TaskHolder {
    private final List<TaskerTask> runningTasks = new LinkedList<>();

    public List<TaskerTask> getRunningTasks() {
        return List.copyOf(runningTasks);
    }

    public Tasker.TaskBuilder build(Runnable runnable) {
        return Tasker.build(runnable)
                .startEvent(runningTasks::add)
                .stopEvent(runningTasks::remove);
    }

    public void cancelAllTasks() {
        runningTasks.forEach(TaskerTask::cancel);
    }
}
