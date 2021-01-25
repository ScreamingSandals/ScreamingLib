package org.screamingsandals.lib.bungee.tasker;

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
        ScheduledTask task = null;

        if (builder.isAfterOneTick() && builder.isAsync()) {
            throw new UnsupportedOperationException("Todo");
        }

        if (builder.isAfterOneTick()
                || builder.isAsync()) {
            task = scheduler.runAsync(plugin, runnable);
        }

        if (builder.getDelay() > 0) {
            task = scheduler.schedule(plugin, runnable,
                    builder.getTimeUnit().getTime((int) builder.getDelay()), builder.getTimeUnit().getTimeUnit());
        }

        if (builder.getRepeat() > 0) {
            task = scheduler.schedule(plugin, runnable,
                    builder.getTimeUnit().getTime((int) builder.getDelay()),
                    builder.getTimeUnit().getTime((int) builder.getRepeat()),
                    builder.getTimeUnit().getTimeUnit());
        }

        if (task == null) {
            throw new UnsupportedOperationException("Cannot start task " + builder.getTaskId() + "!");
        }

        final var toReturn = new AbstractTaskerTask(tasker, builder.getTaskId(), task) {
        };

        tasker.register(toReturn);
        return toReturn;
    }

    @Override
    public TaskState getState(TaskerTask taskerTask) {
        //TODO: check task
        if (tasker.getRunningTasks().containsKey(taskerTask.getId())) {
            return TaskState.RUNNING;
        }
        return TaskState.FINISHED;
    }
}
