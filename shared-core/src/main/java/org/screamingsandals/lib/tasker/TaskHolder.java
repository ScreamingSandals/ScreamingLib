package org.screamingsandals.lib.tasker;

import org.screamingsandals.lib.tasker.task.TaskerTask;

import java.util.LinkedList;
import java.util.List;

public class TaskHolder {
    private final List<TaskerTask> runningTasks = new LinkedList<>();

    public List<TaskerTask> getRunningTasks() {
        return List.copyOf(runningTasks);
    }

    public Tasker.TaskBuilder build(Runnable runnable) {
        return Tasker.build(runnable)
                .startEvent(runningTasks::add)
                .stopEvent(runningTasks::remove);
    }

    public void cancelAllTasks() {
        runningTasks.forEach(TaskerTask::cancel);
    }
}
