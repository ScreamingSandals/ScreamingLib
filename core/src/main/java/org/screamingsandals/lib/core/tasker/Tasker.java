package org.screamingsandals.lib.core.tasker;

import org.screamingsandals.lib.core.tasker.task.BaseTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper for Bukkit's and Bungee's task runners
 */
public interface Tasker {
    Map<BaseTask, Object> runningTasks = new HashMap<>();

    /**
     * Map of right now running tasks!
     *
     * @return immutable map of active tasks
     */
    Map<BaseTask, Object> getRunningTasks();

    /**
     * Destroys the whole tasker :(
     */
    void destroy();

    /**
     * Destroys the task (that means it stops the task!)
     *
     * @param baseTask the task itself
     */
    void destroyTask(BaseTask baseTask);

    /**
     * Checks if the task has stopped
     *
     * @param baseTask the task itself
     * @return true if task is stopped
     */
    boolean hasStopped(BaseTask baseTask);

    /**
     * Usable only for Bukkit!
     * Runs the task on next server tick
     *
     * @param baseTask instance of {@link BaseTask}
     * @return baked {@link BaseTask}
     */
    BaseTask runTask(BaseTask baseTask);

    /**
     * Runs the task asynchronously
     *
     * @param baseTask instance of {@link BaseTask}
     * @return baked {@link BaseTask}
     */
    BaseTask runTaskAsync(BaseTask baseTask);

    /**
     * Runs the task after some delay
     *
     * @param baseTask   instance of {@link BaseTask}
     * @param delay      how later should we run
     * @param taskerUnit unit for running task later
     * @return baked {@link BaseTask}
     */
    BaseTask runTaskLater(BaseTask baseTask, int delay, TaskerUnit taskerUnit);

    /**
     * Runs the task in loop after some delay and in some period
     *
     * @param baseTask   instance of {@link BaseTask}
     * @param delay      how later should we run
     * @param period     period for repeating the task
     * @param taskerUnit unit for running task later
     * @return baked {@link BaseTask}
     */
    BaseTask runTaskRepeater(BaseTask baseTask, int delay, int period, TaskerUnit taskerUnit);
}