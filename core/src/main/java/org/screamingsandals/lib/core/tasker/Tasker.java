package org.screamingsandals.lib.core.tasker;

import org.screamingsandals.lib.core.reflect.SReflect;

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
                var task = runningTasks.get(baseTask);
                SReflect.fastInvoke(task, "cancel");
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
            var task = runningTasks.get(baseTask);
            SReflect.fastInvoke(task, "cancel");
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