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

package org.screamingsandals.lib.tasker.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.tasker.Tasker;

import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
public abstract class AbstractTaskerTask implements TaskerTask {
    @Getter
    private final @NotNull Integer id;
    @Getter
    private final @NotNull Object taskObject;
    @Getter
    private final @NotNull List<@NotNull Consumer<@NotNull TaskerTask>> taskEndHandlers;

    private @NotNull TaskState state = TaskState.SCHEDULED;

    public static AbstractTaskerTask of(@NotNull Integer id, @NotNull Object taskObject, @NotNull List<@NotNull Consumer<@NotNull TaskerTask>> taskEndHandlers) {
        final var task = new AbstractTaskerTask(id, taskObject, taskEndHandlers) {
        };
        Tasker.register(task);
        return task;
    }

    public @NotNull TaskState getState() {
        if (state == TaskState.SCHEDULED) {
            return state;
        }

        if (state == TaskState.CANCELLED) {
            return state;
        }

        state = Tasker.getState(this);
        return state;
    }

    @Override
    public void cancel() {
        Tasker.cancel(this);
    }

}
