package org.screamingsandals.lib.minestom.tasker;

import com.google.common.base.Preconditions;
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
        if (builder.isAfterOneTick()) {
            final var taskBuilder = scheduler.buildTask(runnable);
            taskBuilder.delay(TaskerTime.TICKS.getTime(1), convert(TaskerTime.TICKS));
            return AbstractTaskerTask.of(builder.getTaskId(), taskBuilder.schedule());
        }

        if (builder.isAsync()
                && builder.getRepeat() == 0
                && builder.getDelay() == 0) {
            return AbstractTaskerTask.of(builder.getTaskId(), scheduler.buildTask(runnable).schedule());
        }

        final var timeUnit = Preconditions.checkNotNull(builder.getTimeUnit(), "TimeUnit cannot be null!");
        if (builder.getDelay() > 0 && builder.getRepeat() <= 0) {
            return AbstractTaskerTask.of(builder.getTaskId(), scheduler.buildTask(runnable)
                    .delay(builder.getDelay(), convert(timeUnit))
                    .schedule());
        }

        if (builder.getRepeat() > 0) {
            return AbstractTaskerTask.of(builder.getTaskId(), scheduler.buildTask(runnable)
                    .delay(builder.getDelay(), convert(timeUnit))
                    .repeat(builder.getRepeat(), convert(timeUnit))
                    .schedule());
        }

        throw new UnsupportedOperationException("Unsupported Tasker state!");
    }

    @Override
    public TaskState getState(TaskerTask taskerTask) {
        final Task task = taskerTask.getTaskObject();
        return convert(task.getStatus());
    }

    @Override
    public void cancel(TaskerTask task) {
        final Task toCancel = task.getTaskObject();
        toCancel.cancel();
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
