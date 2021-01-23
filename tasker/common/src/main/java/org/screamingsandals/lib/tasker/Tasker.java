package org.screamingsandals.lib.tasker;

import org.screamingsandals.lib.tasker.initializer.AbstractTaskInitializer;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.TaskerTask;

import java.util.Map;
import java.util.function.Supplier;

public interface Tasker {

    static void init(Supplier<AbstractTaskInitializer> taskInitializer) {
        final var initializer = taskInitializer.get();
        final var tasker = new TaskerImpl(initializer);
        initializer.setTasker(tasker);

        if (TaskerImpl.instance != null) {
            throw new UnsupportedOperationException("Tasker is already initialized!");
        }

        TaskerImpl.instance = tasker;
    }

    static Tasker get() {
        return TaskerImpl.instance;
    }

    /**
     * Creates new TaskBuilder for given task and plugin wrapper
     *
     * @param runnable the runnable to run
     * @return new TaskBuilder
     */
    TaskBuilder build(Runnable runnable);

    /**
     * Returns active tasks.
     *
     * @return immutable map of active tasks.
     */
    Map<Integer, TaskerTask> getRunningTasks();

    /**
     * Cancels all tasks.
     */
    void cancelAll();

    /**
     * Cancels given task
     *
     * @param taskerTask the task to cancel
     */
    void cancel(TaskerTask taskerTask);

    /**
     * Registers new task into the Tasker
     *
     * @param taskerTask task to register
     * @return true if task was registered
     */
    boolean register(TaskerTask taskerTask);

    /**
     * @param taskerTask task to check
     * @return Current state of the task
     */
    TaskState getState(TaskerTask taskerTask);

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
