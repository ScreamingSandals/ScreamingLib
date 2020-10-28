package org.screamingsandals.lib.core.tasker.type;

import com.google.inject.Inject;
import com.velocitypowered.api.scheduler.ScheduledTask;
import com.velocitypowered.api.scheduler.Scheduler;
import com.velocitypowered.api.scheduler.TaskStatus;
import org.screamingsandals.lib.core.tasker.Tasker;
import org.screamingsandals.lib.core.tasker.TaskerUnit;
import org.screamingsandals.lib.core.tasker.task.BaseTask;
import org.screamingsandals.lib.core.wrapper.plugin.PluginWrapper;

public class VelocityTasker extends AbstractTasker implements Tasker {
    private final PluginWrapper pluginWrapper;
    private final Scheduler scheduler;

    @Inject
    public VelocityTasker(PluginWrapper pluginWrapper, Scheduler scheduler) {
        this.pluginWrapper = pluginWrapper;
        this.scheduler = scheduler;
    }

    @Override
    public boolean hasStopped(BaseTask baseTask) {
        final var task = (ScheduledTask) baseTask;
        return task.status() == TaskStatus.FINISHED ||
                task.status() == TaskStatus.CANCELLED;
    }

    @Override
    public BaseTask runTask(BaseTask baseTask) {
        return runTaskAsync(baseTask);
    }

    @Override
    public BaseTask runTaskAsync(BaseTask baseTask) {
        final var builder = scheduler.buildTask(pluginWrapper.getPlugin(), baseTask);
        final var scheduled = builder.schedule();

        getRunningTasks().put(baseTask, scheduled);
        return baseTask;
    }

    @Override
    public BaseTask runTaskLater(BaseTask baseTask, int delay, TaskerUnit taskerUnit) {
        final var builder = scheduler.buildTask(pluginWrapper.getPlugin(), baseTask);
        builder.delay(taskerUnit.getTime(delay), taskerUnit.getTimeUnit());

        final var scheduled = builder.schedule();

        getRunningTasks().put(baseTask, scheduled);
        return baseTask;
    }

    @Override
    public BaseTask runTaskRepeater(BaseTask baseTask, int delay, int period, TaskerUnit taskerUnit) {
        final var builder = scheduler.buildTask(pluginWrapper.getPlugin(), baseTask);
        builder.delay(taskerUnit.getTime(delay), taskerUnit.getTimeUnit());
        builder.repeat(taskerUnit.getTime(period), taskerUnit.getTimeUnit());

        final var scheduled = builder.schedule();

        getRunningTasks().put(baseTask, scheduled);
        return baseTask;
    }
}
