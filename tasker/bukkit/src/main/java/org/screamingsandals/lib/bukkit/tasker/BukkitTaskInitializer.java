package org.screamingsandals.lib.bukkit.tasker;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.screamingsandals.lib.tasker.TaskBuilderImpl;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.initializer.AbstractTaskInitializer;
import org.screamingsandals.lib.tasker.task.AbstractTaskerTask;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class BukkitTaskInitializer extends AbstractTaskInitializer {
    private final Plugin plugin;
    private final BukkitScheduler scheduler;

    public static void init(Plugin plugin) {
        Tasker.init(() -> new BukkitTaskInitializer(plugin));
    }

    public BukkitTaskInitializer(Plugin plugin) {
        this.plugin = plugin;
        this.scheduler = plugin.getServer().getScheduler();
    }

    @Override
    public TaskerTask start(TaskBuilderImpl builder) {
        final var runnable = builder.getRunnable();
        BukkitTask task = null;

        if (builder.isAfterOneTick() && builder.isAsync()) {
            throw new UnsupportedOperationException("This is not supported sadly.");
        }

        if (builder.isAfterOneTick()) {
            task = scheduler.runTask(plugin, runnable);
        }

        if (builder.isAsync()
                && builder.getRepeat() == 0
                && builder.getDelay() == 0) {
            task = scheduler.runTaskAsynchronously(plugin, runnable);
        }

        if (builder.getDelay() > 0) {
            if (builder.isAsync()) {
                task = scheduler.runTaskLaterAsynchronously(plugin, runnable,
                        builder.getTimeUnit().getBukkitTime(builder.getDelay()));
            } else {
                task = scheduler.runTaskLater(plugin, runnable,
                        builder.getTimeUnit().getBukkitTime(builder.getDelay()));
            }
        }

        if (builder.getRepeat() > 0) {
            if (builder.isAsync()) {
                task = scheduler.runTaskTimerAsynchronously(plugin, runnable,
                        builder.getTimeUnit().getBukkitTime(builder.getDelay()),
                        builder.getTimeUnit().getBukkitTime(builder.getRepeat()));
            } else {
                task = scheduler.runTaskTimer(plugin, runnable,
                        builder.getTimeUnit().getBukkitTime(builder.getDelay()),
                        builder.getTimeUnit().getBukkitTime(builder.getRepeat()));
            }
        }

        if (task == null) {
            throw new UnsupportedOperationException("Cannot start task " + builder.getTaskId() + "!");
        }

        final var toReturn = new AbstractTaskerTask(builder.getTaskId(), task) {
        };

        Tasker.register(toReturn);
        return toReturn;
    }

    @Override
    public TaskState getState(TaskerTask taskerTask) {
        final BukkitTask task = taskerTask.getTaskObject();
        if (task.isCancelled()) {
            return TaskState.FINISHED;
        }

        return TaskState.RUNNING;
    }

    @Override
    public void cancel(TaskerTask task) {
        final BukkitTask toCancel = task.getTaskObject();
        toCancel.cancel();
    }
}
