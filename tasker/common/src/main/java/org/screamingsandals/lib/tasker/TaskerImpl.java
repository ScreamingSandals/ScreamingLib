package org.screamingsandals.lib.tasker;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.tasker.initializer.AbstractTaskInitializer;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
class TaskerImpl implements Tasker {
    private final AtomicInteger counter = new AtomicInteger(0);
    private final Map<Integer, TaskerTask> runningTasks = new ConcurrentHashMap<>();
    protected final AbstractTaskInitializer initializer;
    protected static Tasker instance;

    @Override
    public TaskBuilder build(Runnable runnable) {
        return new TaskBuilderImpl(runnable, initializer, counter.incrementAndGet());
    }

    @Override
    public Map<Integer, TaskerTask> getRunningTasks() {
        return Map.copyOf(runningTasks);
    }

    @Override
    public void cancelAll() {
        final var tasks = getRunningTasks().values();
        tasks.forEach(this::cancel);
    }

    @Override
    public void cancel(TaskerTask taskerTask) {
        final var task = runningTasks.get(taskerTask.getId());

        if (task == null) {
            return;
        }

        try {
            Reflect.fastInvoke(task, "cancel");
        } catch (Exception e) {
            System.out.println("Exception while cancelling task " + taskerTask.getId() + "!");
            e.printStackTrace();
        }

        runningTasks.remove(task.getId());
    }

    @Override
    public boolean register(@NotNull TaskerTask taskerTask) {
        final var id = taskerTask.getId();
        if (runningTasks.containsKey(id)) {
            return false;
        }

        runningTasks.putIfAbsent(id, taskerTask);
        return true;
    }

    @Override
    public TaskState getState(TaskerTask taskerTask) {
        return initializer.getState(taskerTask);
    }
}
