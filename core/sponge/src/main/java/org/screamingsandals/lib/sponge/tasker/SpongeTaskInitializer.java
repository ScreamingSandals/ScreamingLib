package org.screamingsandals.lib.sponge.tasker;

import org.screamingsandals.lib.tasker.TaskBuilderImpl;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.initializer.AbstractTaskInitializer;
import org.screamingsandals.lib.tasker.task.AbstractTaskerTask;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.Service;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.plugin.PluginContainer;

@Service
public class SpongeTaskInitializer extends AbstractTaskInitializer {
    private final Scheduler scheduler = Sponge.getServer().getScheduler();
    private final Scheduler asyncScheduler = Sponge.getAsyncScheduler();
    private final PluginContainer plugin;

    public SpongeTaskInitializer(Controllable controllable, PluginContainer plugin) {
        super(controllable);
        this.plugin = plugin;
    }

    public static void init(Controllable controllable, PluginContainer plugin) {
        Tasker.init(() -> new SpongeTaskInitializer(controllable, plugin));
    }

    @Override
    public TaskerTask start(TaskBuilderImpl builder) {
        final var runnable = builder.getRunnable();
        ScheduledTask task = null;

        if (builder.isAfterOneTick()) {
            task = scheduler.submit(Task.builder()
                    .plugin(plugin)
                    .delay(Ticks.of(1))
                    .build());
        }

        if (builder.isAsync()) {
            task = asyncScheduler.submit(Task.builder()
                    .plugin(plugin)
                    .build());
        }

        if (builder.getDelay() > 0) {
            task = asyncScheduler.submit(Task.builder()
                    .delay(builder.getTimeUnit().getTime((int) builder.getDelay()), builder.getTimeUnit().getTimeUnit())
                    .plugin(plugin)
                    .build());
        }

        if (builder.getRepeat() > 0) {
            task = asyncScheduler.submit(Task.builder()
                    .delay(builder.getTimeUnit().getTime((int) builder.getDelay()), builder.getTimeUnit().getTimeUnit())
                    .interval(builder.getTimeUnit().getTime((int) builder.getRepeat()), builder.getTimeUnit().getTimeUnit())
                    .plugin(plugin)
                    .build());
        }

        if (task == null) {
            throw new UnsupportedOperationException("Cannot start task " + builder.getTaskId() + "!");
        }

        final var toReturn = new AbstractTaskerTask(builder.getTaskId(), task, builder.getStopEvent()) {
        };

        Tasker.register(toReturn);
        return toReturn;
    }

    @Override
    public TaskState getState(TaskerTask taskerTask) {
        final ScheduledTask task = taskerTask.getTaskObject();

        //TODO
        return TaskState.RUNNING;
    }

    @Override
    public void cancel(TaskerTask task) {
        final ScheduledTask toCancel = task.getTaskObject();
        toCancel.cancel();
    }
}
