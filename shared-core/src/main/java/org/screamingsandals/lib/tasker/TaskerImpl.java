/*
 * Copyright 2022 ScreamingSandals
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

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.tasker.initializer.AbstractTaskInitializer;
import org.screamingsandals.lib.tasker.task.TaskBase;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.TaskerTask;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@RequiredArgsConstructor
class TaskerImpl implements Tasker {
    private final AtomicInteger counter = new AtomicInteger(0);
    private final Map<Integer, TaskerTask> runningTasks = new ConcurrentHashMap<>();
    protected final AbstractTaskInitializer initializer;
    protected static TaskerImpl instance;

    TaskBuilder build(Runnable runnable) {
        return new TaskBuilderImpl(runnable, initializer, counter.incrementAndGet());
    }

    TaskBuilder build(Function<TaskBase, Runnable> taskBase) {
        final var id = counter.incrementAndGet();
        return new TaskBuilderImpl(taskBase.apply(() -> {
            final var task = runningTasks.get(id);
            if (task != null) {
                task.cancel();
            }
        }), initializer, id);
    }

    public Map<Integer, TaskerTask> getRunningTasks() {
        return Map.copyOf(runningTasks);
    }

    public void cancelAll() {
        final var tasks = getRunningTasks().values();
        tasks.forEach(this::cancel);
    }

    public void cancel(TaskerTask taskerTask) {
        final var task = runningTasks.get(taskerTask.getId());

        if (task == null) {
            return;
        }

        try {
            initializer.cancel(taskerTask);
        } catch (Exception e) {
            throw new UnsupportedOperationException("Exception while cancelling task " + taskerTask.getId() + "!", e);
        }

        runningTasks.remove(task.getId());
    }

    public boolean register(@NotNull TaskerTask taskerTask) {
        final var id = taskerTask.getId();
        if (runningTasks.containsKey(id)) {
            return false;
        }

        runningTasks.putIfAbsent(id, taskerTask);
        return true;
    }

    public TaskState getState(TaskerTask taskerTask) {
        return initializer.getState(taskerTask);
    }
}
