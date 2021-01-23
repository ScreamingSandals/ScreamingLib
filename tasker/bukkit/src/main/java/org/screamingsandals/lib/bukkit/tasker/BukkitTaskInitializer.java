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
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.PlatformMapping;

@PlatformMapping(platform = PlatformType.BUKKIT)
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
            throw new UnsupportedOperationException("Todo");
        }

        if (builder.isAfterOneTick()) {
            task = scheduler.runTask(plugin, runnable);
        }

        if (builder.isAsync()) {
            task = scheduler.runTaskAsynchronously(plugin, runnable);
        }

        if (builder.getDelay() > 0) {
            task = scheduler.runTaskLater(plugin, runnable,
                    builder.getTimeUnit().getBukkitTime(builder.getDelay()));
        }

        if (builder.getRepeat() > 0) {
            task = scheduler.runTaskTimer(plugin, runnable,
                    builder.getTimeUnit().getBukkitTime(builder.getDelay()),
                    builder.getTimeUnit().getBukkitTime(builder.getRepeat()));
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
        final BukkitTask task = taskerTask.getTaskObject();
        if (task.isCancelled()) {
            return TaskState.FINISHED;
        }

        return TaskState.RUNNING;
    }
}
