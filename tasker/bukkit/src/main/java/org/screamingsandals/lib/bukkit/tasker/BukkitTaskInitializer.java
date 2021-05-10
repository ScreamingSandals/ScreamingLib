package org.screamingsandals.lib.bukkit.tasker;

import com.google.common.base.Preconditions;
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

        if (builder.isAsync()
                && !builder.isAfterOneTick()
                && builder.getRepeat() == 0
                && builder.getDelay() == 0) {
            return AbstractTaskerTask.of(builder.getTaskId(), scheduler.runTaskAsynchronously(plugin, runnable), builder.getStopEvent());
        }

        if (builder.isAfterOneTick()) {
            return AbstractTaskerTask.of(builder.getTaskId(), scheduler.runTask(plugin, runnable), builder.getStopEvent());
        }

        final var timeUnit = Preconditions.checkNotNull(builder.getTimeUnit(), "TimeUnit cannot be null!");
        if (builder.getDelay() > 0 && builder.getRepeat() <= 0) {
            if (builder.isAsync()) {
                return AbstractTaskerTask.of(builder.getTaskId(), scheduler.runTaskLaterAsynchronously(plugin, runnable,
                        timeUnit.getBukkitTime(builder.getDelay())), builder.getStopEvent());
            } else {
                return AbstractTaskerTask.of(builder.getTaskId(), scheduler.runTaskLater(plugin, runnable,
                        timeUnit.getBukkitTime(builder.getDelay())), builder.getStopEvent());
            }
        }

        if (builder.getRepeat() > 0) {
            if (builder.isAsync()) {
                return AbstractTaskerTask.of(builder.getTaskId(), scheduler.runTaskTimerAsynchronously(plugin, runnable,
                        timeUnit.getBukkitTime(builder.getDelay()),
                        timeUnit.getBukkitTime(builder.getRepeat())), builder.getStopEvent());
            } else {
                return AbstractTaskerTask.of(builder.getTaskId(), scheduler.runTaskTimer(plugin, runnable,
                        timeUnit.getBukkitTime(builder.getDelay()),
                        timeUnit.getBukkitTime(builder.getRepeat())), builder.getStopEvent());
            }
        }

        throw new UnsupportedOperationException("Unsupported Tasker state!");
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
