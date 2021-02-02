package org.screamingsandals.lib.minestom.tasker;

import lombok.RequiredArgsConstructor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extensions.Extension;
import net.minestom.server.timer.SchedulerManager;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskStatus;
import net.minestom.server.utils.time.TimeUnit;
import org.screamingsandals.lib.tasker.TaskBuilderImpl;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.tasker.initializer.AbstractTaskInitializer;
import org.screamingsandals.lib.tasker.task.AbstractTaskerTask;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
@RequiredArgsConstructor
public class MinestomTaskInitializer extends AbstractTaskInitializer {
    private final SchedulerManager scheduler = MinecraftServer.getSchedulerManager();
    private final Extension plugin;

    public static void init(Extension plugin) {
        Tasker.init(() -> new MinestomTaskInitializer(plugin));
    }

    @Override
    public TaskerTask start(TaskBuilderImpl builder) {
        final var runnable = builder.getRunnable();
        Task task = null;

        if (builder.isAfterOneTick() || builder.isAsync()) {
            task = scheduler.buildTask(runnable).schedule();
        }

        if (builder.getDelay() > 0) {
            task = scheduler.buildTask(runnable)
                    .delay(builder.getDelay(), convert(builder.getTimeUnit()))
                    .schedule();
        }

        if (builder.getRepeat() > 0) {
            task = scheduler.buildTask(runnable)
                    .delay(builder.getDelay(), convert(builder.getTimeUnit()))
                    .repeat(builder.getRepeat(), convert(builder.getTimeUnit()))
                    .schedule();
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
        final Task task = taskerTask.getTaskObject();
        return convert(task.getStatus());
    }

    private TimeUnit convert(TaskerTime time) {
        switch (time) {
            case SECONDS:
                return TimeUnit.SECOND;
            case MINUTES:
                return TimeUnit.MINUTE;
            case HOURS:
                return TimeUnit.HOUR;
            default:
                return TimeUnit.TICK;
        }
    }

    private TaskState convert(TaskStatus status) {
        switch (status) {
            case FINISHED:
                return TaskState.FINISHED;
            case CANCELLED:
                return TaskState.CANCELLED;
            case SCHEDULED:
                return TaskState.SCHEDULED;
            default:
                return TaskState.RUNNING;
        }
    }
}
