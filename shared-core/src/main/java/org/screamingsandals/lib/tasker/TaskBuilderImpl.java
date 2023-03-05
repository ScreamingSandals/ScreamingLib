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
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.Preconditions;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Getter
public class TaskBuilderImpl implements Tasker.TaskBuilder {
    private final @NotNull Runnable runnable;
    private final @NotNull Integer taskId;
    private final @NotNull List<@NotNull Consumer<@NotNull TaskerTask>> startEvent = new LinkedList<>();

    private boolean afterOneTick;
    private boolean async;
    private long delay;
    private long repeat;

    private @NotNull TaskerTime timeUnit = TaskerTime.TICKS;

    @Override
    public Tasker.@NotNull TaskBuilder afterOneTick() {
        if (async) {
            throw new UnsupportedOperationException("Cannot be delayed after one tick, the task is async!");
        }
        afterOneTick = true;
        return this;
    }

    @Override
    public Tasker.@NotNull TaskBuilder async() {
        if (afterOneTick) {
            throw new UnsupportedOperationException("Cannot be async, the task is delayed after one tick!");
        }
        async = true;
        return this;
    }

    @Override
    public Tasker.@NotNull TaskBuilder delay(long time, @NotNull TaskerTime unit) {
        Preconditions.checkArgument(time >= 0, "Time needs to be equals or bigger than 0!");
        delay = time;
        timeUnit = unit;
        return this;
    }

    @Override
    public Tasker.@NotNull TaskBuilder repeat(long time, @NotNull TaskerTime unit) {
        Preconditions.checkArgument(time >= 0, "Time needs to be equals or bigger than 0!");
        repeat = time;
        timeUnit = unit;
        return this;
    }

    @Override
    public Tasker.@NotNull TaskBuilder startEvent(@NotNull Consumer<@NotNull TaskerTask> handler) {
        Preconditions.checkNotNull(handler, "Handler can't be null!");
        this.startEvent.add(handler);
        return this;
    }

    @Override
    public @NotNull TaskerTask start() {
        return Preconditions.checkNotNull(
                Tasker.startAndRegisterTask(this),
                "Error occurred while trying to run task number " + taskId
        );
    }
}
