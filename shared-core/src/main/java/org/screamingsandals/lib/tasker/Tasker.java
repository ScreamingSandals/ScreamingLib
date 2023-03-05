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

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.tasker.task.TaskBase;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.methods.OnDisable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

@AbstractService
public abstract class Tasker {
    private static @Nullable Tasker instance;

    private final @NotNull AtomicInteger counter = new AtomicInteger(0);
    private final @NotNull Map<@NotNull Integer, TaskerTask> runningTasks = new ConcurrentHashMap<>();

    @ApiStatus.Internal
    protected Tasker() {
        if (instance != null) {
            throw new UnsupportedOperationException("Tasker is already initialized!");
        }

        instance = this;
    }

    /**
     * Creates new TaskBuilder for given task and plugin wrapper
     *
     * @param runnable the runnable to run
     * @return new TaskBuilder
     */
    public static @NotNull TaskBuilder build(@NotNull Runnable runnable) {
        if (instance == null) {
            throw new UnsupportedOperationException("Tasker is not initialized yet!");
        }
        return new TaskBuilderImpl(runnable, instance.counter.incrementAndGet());
    }

    /**
     * Creates new TaskBuilder for given task and plugin wrapper
     * This task can cancel itself :)
     *
     * @param taskBase the runnable to run
     * @return new TaskBuilder
     */
    public static @NotNull TaskBuilder build(@NotNull Function<@NotNull TaskBase, @NotNull Runnable> taskBase) {
        if (instance == null) {
            throw new UnsupportedOperationException("Tasker is not initialized yet!");
        }
        final var id = instance.counter.incrementAndGet();
        return new TaskBuilderImpl(taskBase.apply(() -> {
            final var task = instance.runningTasks.get(id);
            if (task != null) {
                task.cancel();
            }
        }), id);
    }

    /**
     * Returns active tasks.
     *
     * @return immutable map of active tasks.
     */
    public static @NotNull Map<@NotNull Integer, TaskerTask> getRunningTasks() {
        if (instance == null) {
            throw new UnsupportedOperationException("Tasker is not initialized yet!");
        }
        return Map.copyOf(instance.runningTasks);
    }

    /**
     * Cancels all tasks.
     */
    @OnDisable
    public static void cancelAll() {
        if (instance == null) {
            throw new UnsupportedOperationException("Tasker is not initialized yet!");
        }
        final var tasks = getRunningTasks().values();
        tasks.forEach(TaskerTask::cancel);
    }

    /**
     * Registers new task into the Tasker
     *
     * @param taskerTask task to register
     * @return true if task was registered
     */
    public static boolean register(@NotNull TaskerTask taskerTask) {
        if (instance == null) {
            throw new UnsupportedOperationException("Tasker is not initialized yet!");
        }
        final var id = taskerTask.getId();
        if (instance.runningTasks.containsKey(id)) {
            return false;
        }

        instance.runningTasks.putIfAbsent(id, taskerTask);
        return true;
    }

    @ApiStatus.Internal
    public static boolean unregister(@NotNull TaskerTask taskerTask) {
        if (instance == null) {
            throw new UnsupportedOperationException("Tasker is not initialized yet!");
        }
        final var id = taskerTask.getId();
        if (!instance.runningTasks.containsKey(id)) {
            return false;
        }

        instance.runningTasks.remove(id);
        return true;
    }

    public static @NotNull TaskerTask startAndRegisterTask(@NotNull TaskBuilderImpl builder) {
        if (instance == null) {
            throw new UnsupportedOperationException("Tasker is not initialized yet!");
        }
        Preconditions.checkNotNull(builder, "Builder can't be null");
        var task = instance.start0(builder);
        Tasker.register(task);
        builder.getStartEvent().forEach(h -> h.accept(task));
        return task;
    }

    protected abstract @NotNull TaskerTask start0(@NotNull TaskBuilderImpl taskerBuilder);

    /**
     * Builder for tasks
     */
    public interface TaskBuilder {

        /**
         * Runs the task after 1 tick (50ms)
         *
         * @return current task builder
         */
        @Contract("-> this")
        @NotNull TaskBuilder afterOneTick();

        /**
         * Runs the task async
         *
         * @return current task builder
         */
        @Contract("-> this")
        @NotNull TaskBuilder async();

        /**
         * Runs the task after given delay
         *
         * @param time time
         * @param unit unit
         * @return current task builder
         */
        @Contract("_, _ -> this")
        @NotNull TaskBuilder delay(long time, @NotNull TaskerTime unit);

        /**
         * Runs the task repeatedly within given repeat time
         *
         * @param time time
         * @return current task builder
         */
        @Contract("_, _ -> this")
        @NotNull TaskBuilder repeat(long time, @NotNull TaskerTime unit);

        /**
         * Registers handler that will be used after starting the task.
         *
         * @param handler Handler
         * @return current task builder
         */
        @Contract("_ -> this")
        @NotNull TaskBuilder startEvent(@NotNull Consumer<@NotNull TaskerTask> handler);

        /**
         * Starts the task
         */
        @Contract("-> new")
        @NotNull TaskerTask start();
    }
}
