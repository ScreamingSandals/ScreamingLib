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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.tasker.initializer.AbstractTaskInitializer;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.Preconditions;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Getter
public class TaskBuilderImpl implements Tasker.TaskBuilder {
    private final Runnable runnable;
    private final AbstractTaskInitializer initializer;
    private final Integer taskId;
    private final List<Consumer<TaskerTask>> startEvent = new LinkedList<>();
    private final List<Consumer<TaskerTask>> stopEvent = new LinkedList<>();

    private boolean afterOneTick = false;
    private boolean async = false;
    private long delay;
    private long repeat;

    private TaskerTime timeUnit = TaskerTime.TICKS;

    @Override
    public Tasker.TaskBuilder afterOneTick() {
        if (async) {
            throw new UnsupportedOperationException("Cannot be delayed after one tick, the task is async!");
        }
        afterOneTick = true;
        return this;
    }

    @Override
    public Tasker.TaskBuilder async() {
        if (afterOneTick) {
            throw new UnsupportedOperationException("Cannot be async, the task is delayed after one tick!");
        }
        async = true;
        return this;
    }

    @Override
    public Tasker.TaskBuilder delay(long time, TaskerTime unit) {
        Preconditions.checkArgument(time >= 0, "Time needs to be equals or bigger than 0!");
        delay = time;
        timeUnit = unit;
        return this;
    }

    @Override
    public Tasker.TaskBuilder repeat(long time, TaskerTime unit) {
        Preconditions.checkArgument(time >= 0, "Time needs to be equals or bigger than 0!");
        repeat = time;
        timeUnit = unit;
        return this;
    }

    @Override
    public Tasker.TaskBuilder startEvent(Consumer<TaskerTask> handler) {
        Preconditions.checkNotNull(handler, "Handler can't be null!");
        this.startEvent.add(handler);
        return this;
    }

    @Override
    public Tasker.TaskBuilder stopEvent(Consumer<TaskerTask> handler) {
        Preconditions.checkNotNull(handler, "Handler can't be null!");
        this.stopEvent.add(handler);
        return this;
    }

    @Override
    public TaskerTask start() {
        final var task = Preconditions.checkNotNull(
                initializer.start(this),
                "Error occurred while trying to run task number " + taskId);
        Tasker.register(task);
        this.startEvent.forEach(h -> h.accept(task));
        return task;
    }
}
