package org.screamingsandals.lib.core.tasker;

import com.google.common.collect.ImmutableMap;
import org.screamingsandals.lib.core.reflect.SReflect;

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
    default Map<BaseTask, Object> getRunningTasks() {
        return ImmutableMap.copyOf(runningTasks);
    }

    /**
     * Destroys the whole tasker :(
     */
    default void destroy() {
        getRunningTasks().keySet().forEach(baseTask -> {
            if (baseTask == null) {
                return;
            }

            try {
                var task = runningTasks.get(baseTask);
                SReflect.fastInvoke(task, "cancel");
            } catch (Exception ignored) {
            }
        });

        runningTasks.clear();
    }

    /**
     * Destroys the task (that means it stops the task!)
     *
     * @param baseTask the task itself
     */
    default void destroyTask(BaseTask baseTask) {
        if (baseTask == null) {
            return;
        }

        try {
            var task = runningTasks.get(baseTask);
            SReflect.fastInvoke(task, "cancel");
        } catch (Exception ignored) {
        }

        runningTasks.remove(baseTask);
    }

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
     * @param taskerTime unit for running task later
     * @return baked {@link BaseTask}
     */
    BaseTask runTaskLater(BaseTask baseTask, int delay, TaskerTime taskerTime);

    /**
     * Runs the task in loop after some delay and in some period
     *
     * @param baseTask   instance of {@link BaseTask}
     * @param delay      how later should we run
     * @param period     period for repeating the task
     * @param taskerTime unit for running task later
     * @return baked {@link BaseTask}
     */
    BaseTask runTaskRepeater(BaseTask baseTask, int delay, int period, TaskerTime taskerTime);
}