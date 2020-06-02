package org.screamingsandals.lib.tasker;

import java.util.HashMap;
import java.util.Map;

public interface Tasker {
    Map<BaseTask, Object> runningTasks = new HashMap<>();

    default Map<BaseTask, Object> getRunningTasks() {
        return runningTasks;
    }

    default void destroy() {
        getRunningTasks().keySet().forEach(baseTask -> {
            if (baseTask == null) {
                return;
            }

            try {
                Object task = runningTasks.get(baseTask);
                task.getClass().getMethod("cancel").invoke(task);
            } catch (Exception ignored) {
            }
        });

        runningTasks.clear();
    }

    default void destroyTask(BaseTask baseTask) {
        if (baseTask == null) {
            return;
        }

        try {
            Object task = runningTasks.get(baseTask);
            task.getClass().getMethod("cancel").invoke(task);
        } catch (Exception ignored) {
        }

        runningTasks.remove(baseTask);
    }

    boolean hasStopped(BaseTask baseTask);

    BaseTask runTask(BaseTask baseTask);

    BaseTask runTaskAsync(BaseTask baseTask);

    BaseTask runTaskLater(BaseTask baseTask, int delay, TaskerTime taskerTime);

    BaseTask runTaskRepeater(BaseTask baseTask, int delay, int period, TaskerTime taskerTime);

    static Tasker getSpigot(Object plugin) {
        return new TaskerWrapper.SpigotTasker(plugin);
    }

    static Tasker getBungee(Object plugin) {
        return new TaskerWrapper.BungeeTasker(plugin);
    }
}