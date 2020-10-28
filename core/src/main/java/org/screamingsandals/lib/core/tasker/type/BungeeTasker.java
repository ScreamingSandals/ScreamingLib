package org.screamingsandals.lib.core.tasker.type;

import com.google.inject.Inject;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import org.screamingsandals.lib.core.tasker.Tasker;
import org.screamingsandals.lib.core.tasker.TaskerUnit;
import org.screamingsandals.lib.core.tasker.task.BaseTask;
import org.screamingsandals.lib.core.wrapper.plugin.PluginWrapper;

public class BungeeTasker extends AbstractTasker implements Tasker {
    private final Plugin plugin;
    private final TaskScheduler scheduler;

    @Inject
    public BungeeTasker(PluginWrapper pluginWrapper) {
        this.plugin = pluginWrapper.getPlugin();
        this.scheduler = plugin.getProxy().getScheduler();
    }

    @Override
    public boolean hasStopped(BaseTask baseTask) {
        return getRunningTasks().get(baseTask) == null;

        //TODO: make a way to get the tas status
    }

    @Override
    public BaseTask runTask(BaseTask baseTask) {
       return runTaskAsync(baseTask);
    }

    @Override
    public BaseTask runTaskAsync(BaseTask baseTask) {
        try {
            getRunningTasks().put(baseTask, scheduler.runAsync(plugin, baseTask));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseTask;
    }

    @Override
    public BaseTask runTaskLater(BaseTask baseTask, int delay, TaskerUnit taskerUnit) {
        try {
            getRunningTasks().put(baseTask, scheduler.schedule(
                    plugin, baseTask, taskerUnit.getTime(delay), taskerUnit.getTimeUnit()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseTask;
    }

    @Override
    public BaseTask runTaskRepeater(BaseTask baseTask, int delay, int period, TaskerUnit taskerUnit) {
        try {
            getRunningTasks().put(baseTask, scheduler.schedule(
                    plugin, baseTask, taskerUnit.getTime(delay), taskerUnit.getTime(period), taskerUnit.getTimeUnit()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseTask;
    }
}
