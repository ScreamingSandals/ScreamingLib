package org.screamingsandals.lib.bungee.tasker;

import com.google.common.base.Preconditions;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import org.screamingsandals.lib.tasker.TaskBuilderImpl;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.initializer.AbstractTaskInitializer;
import org.screamingsandals.lib.tasker.task.AbstractTaskerTask;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class BungeeTaskInitializer extends AbstractTaskInitializer {
    private final Plugin plugin;
    private final TaskScheduler scheduler;

    public static void init(Plugin plugin) {
        Tasker.init(() -> new BungeeTaskInitializer(plugin));
    }

    public BungeeTaskInitializer(Plugin plugin) {
        this.plugin = plugin;
        this.scheduler = plugin.getProxy().getScheduler();
    }

    @Override
    public TaskerTask start(TaskBuilderImpl builder) {
        final var runnable = builder.getRunnable();
        if (builder.isAfterOneTick()) {
            return AbstractTaskerTask.of(builder.getTaskId(), scheduler.runAsync(plugin, runnable));
        }

        if (builder.isAsync()
                && builder.getRepeat() == 0
                && builder.getDelay() == 0) {
            return AbstractTaskerTask.of(builder.getTaskId(), scheduler.runAsync(plugin, runnable));
        }

        final var timeUnit = Preconditions.checkNotNull(builder.getTimeUnit(), "TimeUnit cannot be null!");
        if (builder.getDelay() > 0 && builder.getRepeat() <= 0) {
            return AbstractTaskerTask.of(builder.getTaskId(), scheduler.schedule(plugin, runnable,
                    builder.getTimeUnit().getTime((int) builder.getDelay()),
                    builder.getTimeUnit().getTimeUnit()));
        }

        if (builder.getRepeat() > 0) {
            return AbstractTaskerTask.of(builder.getTaskId(), scheduler.schedule(plugin, runnable,
                    builder.getTimeUnit().getTime((int) builder.getDelay()),
                    builder.getTimeUnit().getTime((int) builder.getRepeat()),
                    builder.getTimeUnit().getTimeUnit()));
        }

        throw new UnsupportedOperationException("Unsupported Tasker state!");
    }

    @Override
    public TaskState getState(TaskerTask taskerTask) {
        //TODO: check task
        if (Tasker.getRunningTasks().containsKey(taskerTask.getId())) {
            return TaskState.RUNNING;
        }
        return TaskState.FINISHED;
    }

    @Override
    public void cancel(TaskerTask task) {
        final ScheduledTask toCancel = task.getTaskObject();
        toCancel.cancel();
    }
}
