package org.screamingsandals.lib.tasker;

import org.screamingsandals.lib.tasker.initializer.AbstractTaskInitializer;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.TaskerTask;

import java.util.Map;
import java.util.function.Supplier;

public interface Tasker {

    static void init(Supplier<AbstractTaskInitializer> taskInitializer) {
        if (TaskerImpl.instance != null) {
            throw new UnsupportedOperationException("Tasker is already initialized!");
        }

        TaskerImpl.instance = new TaskerImpl(taskInitializer.get());
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
         * Starts the task
         */
        TaskerTask start();
    }
}
