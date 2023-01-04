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

package org.screamingsandals.lib.tasker.initializer;


import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.tasker.TaskBuilderImpl;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.AbstractService;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+)\\.initializer\\.Abstract(?<className>.+)$"
)
public abstract class AbstractTaskInitializer {

    public AbstractTaskInitializer(Controllable controllable) {
        controllable.disable(Tasker::cancelAll);
    }

    public abstract @NotNull TaskerTask start(@NotNull TaskBuilderImpl taskerBuilder);

    public abstract @NotNull TaskState getState(@NotNull TaskerTask taskerTask);

    public abstract void cancel(@NotNull TaskerTask task);
}
