package org.screamingsandals.lib.tasker;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public interface Tasker {
    HashMap<BaseTask, Object> runningTasks = new HashMap<>();

    default HashMap<BaseTask, Object> getRunningTasks() {
        return new HashMap<>(runningTasks);
    }

    default void destroy() {
        getRunningTasks().keySet().forEach(this::destroyTask);
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

    BaseTask runTask(BaseTask baseTask);

    BaseTask runTaskAsync(BaseTask baseTask);

    BaseTask runTaskLater(BaseTask baseTask, long delay, TimeUnit timeUnit);

    BaseTask runTaskRepeater(BaseTask baseTask, long delay, long period, TimeUnit timeUnit);

    default long getBukkitTime(long delay, TimeUnit timeUnit) {
        switch (timeUnit) {
            case SECONDS:
                return delay * 20;
            case MINUTES:
                return delay * 1200;
            case HOURS:
                return delay * 72000;
            default:
                return delay;
        }
    }
}