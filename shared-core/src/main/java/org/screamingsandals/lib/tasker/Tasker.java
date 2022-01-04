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

import org.screamingsandals.lib.tasker.initializer.AbstractTaskInitializer;
import org.screamingsandals.lib.tasker.task.TaskBase;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.annotations.ForwardToService;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@ForwardToService(AbstractTaskInitializer.class)
public interface Tasker {

    static Tasker init(Supplier<AbstractTaskInitializer> taskInitializer) {
        if (TaskerImpl.instance != null) {
            throw new UnsupportedOperationException("Tasker is already initialized!");
        }

        TaskerImpl.instance = new TaskerImpl(taskInitializer.get());
        return TaskerImpl.instance;
    }

    /**
     * Creates new TaskBuilder for given task and plugin wrapper
     *
     * @param runnable the runnable to run
     * @return new TaskBuilder
     */
    static TaskBuilder build(Runnable runnable) {
        return TaskerImpl.instance.build(runnable);
    }

    /**
     * Creates new TaskBuilder for given task and plugin wrapper
     * This task can cancel itself :)
     *
     * @param runnable the runnable to run
     * @return new TaskBuilder
     */
    static TaskBuilder build(Function<TaskBase, Runnable> runnable) {
        return TaskerImpl.instance.build(runnable);
    }

    /**
     * Returns active tasks.
     *
     * @return immutable map of active tasks.
     */
    static Map<Integer, TaskerTask> getRunningTasks() {
        return TaskerImpl.instance.getRunningTasks();
    }

    /**
     * Cancels all tasks.
     */
    static void cancelAll() {
        TaskerImpl.instance.cancelAll();
    }

    /**
     * Cancels given task
     *
     * @param taskerTask the task to cancel
     */
    static void cancel(TaskerTask taskerTask) {
        TaskerImpl.instance.cancel(taskerTask);
    }

    /**
     * Registers new task into the Tasker
     *
     * @param taskerTask task to register
     * @return true if task was registered
     */
    static boolean register(TaskerTask taskerTask) {
        return TaskerImpl.instance.register(taskerTask);
    }

    /**
     * @param taskerTask task to check
     * @return Current state of the task
     */
    static TaskState getState(TaskerTask taskerTask) {
        return TaskerImpl.instance.getState(taskerTask);
    }

    /**
     * Builder for tasks
     */
    interface TaskBuilder {

        /**
         * Runs the task after 1 tick (50ms)
         *
         * @return current task builder
         */
        TaskBuilder afterOneTick();

        /**
         * Runs the task async
         *
         * @return current task builder
         */
        TaskBuilder async();

        /**
         * Runs the task after given delay
         *
         * @param time time
         * @param unit unit
         * @return current task builder
         */
        TaskBuilder delay(long time, TaskerTime unit);

        /**
         * Runs the task repeatedly within given repeat time
         *
         * @param time time
         * @return current task builder
         */
        TaskBuilder repeat(long time, TaskerTime unit);

        /**
         * Registers handler that will be used after starting the task.
         *
         * @param handler Handler
         * @return current task builder
         */
        TaskBuilder startEvent(Consumer<TaskerTask> handler);

        /**
         * Registers handler that will be used after ending the task.
         *
         * @param handler Handler
         * @return current task builder
         */
        TaskBuilder stopEvent(Consumer<TaskerTask> handler);

        /**
         * Starts the task
         */
        TaskerTask start();
    }
}
