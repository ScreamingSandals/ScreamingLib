package org.screamingsandals.lib.core.tasker.type;

import com.google.inject.Inject;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.screamingsandals.lib.core.tasker.Tasker;
import org.screamingsandals.lib.core.tasker.TaskerUnit;
import org.screamingsandals.lib.core.tasker.task.BaseTask;
import org.screamingsandals.lib.core.wrapper.plugin.PluginWrapper;

public class BukkitTasker extends AbstractTasker implements Tasker {
    private final Plugin plugin;
    private final BukkitScheduler scheduler;

    @Inject
    public BukkitTasker(PluginWrapper plugin) {
        this.plugin = plugin.getPlugin();
        this.scheduler = this.plugin.getServer().getScheduler();
    }

    @Override
    public boolean hasStopped(BaseTask baseTask) {
        BukkitTask task = (BukkitTask) getRunningTasks().get(baseTask);
        if (task == null) {
            return true;
        }

        return task.isCancelled();
    }

    @Override
    public BaseTask runTask(BaseTask baseTask) {
        try {
            final var scheduled = scheduler.runTask(plugin, baseTask);

            getRunningTasks().put(baseTask, scheduled);
        } catch (Exception ignored) {
            log.warn("You used Bukkit task on Bungee server, what the heck?!");
        }
        return baseTask;
    }

    @Override
    public BaseTask runTaskAsync(BaseTask baseTask) {
        try {
            final var scheduled = scheduler
                    .runTaskAsynchronously(plugin, baseTask);

            getRunningTasks().put(baseTask, scheduled);
        } catch (Throwable t) {
            log.warn("Exception occurred while executing task! {}", t.getMessage(), t);
        }
        return baseTask;
    }

    @Override
    public BaseTask runTaskLater(BaseTask baseTask, int delay, TaskerUnit taskerUnit) {
        try {
            final var scheduled = scheduler.runTaskLater(
                    plugin, baseTask, taskerUnit.getBukkitTime(delay));

            getRunningTasks().put(baseTask, scheduled);
        } catch (Throwable t) {
            log.warn("Exception occurred while executing task! {}", t.getMessage(), t);
        }
        return baseTask;
    }

    @Override
    public BaseTask runTaskRepeater(BaseTask baseTask, int delay, int period, TaskerUnit taskerUnit) {
        try {
            final var scheduled = scheduler.runTaskTimer(
                    plugin, baseTask, taskerUnit.getBukkitTime(delay), taskerUnit.getBukkitTime(period));

            getRunningTasks().put(baseTask, scheduled);
        } catch (Throwable t) {
            log.warn("Exception occurred while executing task! {}", t.getMessage(), t);
        }
        return baseTask;
    }
}
