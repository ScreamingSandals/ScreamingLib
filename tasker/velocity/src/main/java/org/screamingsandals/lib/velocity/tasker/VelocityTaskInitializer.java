package org.screamingsandals.lib.velocity.tasker;

import com.google.common.base.Preconditions;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import com.velocitypowered.api.scheduler.Scheduler;
import lombok.RequiredArgsConstructor;
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
public class VelocityTaskInitializer extends AbstractTaskInitializer {
    private final Object owner;
    private final Scheduler scheduler;

    public static void init(Object owner, ProxyServer proxyServer) {
        Tasker.init(() -> new VelocityTaskInitializer(owner, proxyServer.getScheduler()));
    }

    @Override
    public TaskerTask start(TaskBuilderImpl builder) {
        final var runnable = builder.getRunnable();

        if (builder.isAfterOneTick()) {
            final var taskBuilder = scheduler.buildTask(owner, runnable);
            taskBuilder.delay(TaskerTime.TICKS.getTime(1), TaskerTime.TICKS.getTimeUnit());
            return AbstractTaskerTask.of(builder.getTaskId(), taskBuilder.schedule(), builder.getStopEvent());
        }

        if (builder.isAsync()
                && builder.getRepeat() == 0
                && builder.getDelay() == 0) {
            return AbstractTaskerTask.of(builder.getTaskId(), scheduler.buildTask(owner, runnable).schedule(), builder.getStopEvent());
        }

        final var timeUnit = Preconditions.checkNotNull(builder.getTimeUnit(), "TimeUnit cannot be null!");
        if (builder.getDelay() > 0 && builder.getRepeat() <= 0) {
            return AbstractTaskerTask.of(builder.getTaskId(), scheduler.buildTask(owner, runnable)
                    .delay(timeUnit.getTime((int) builder.getRepeat()), timeUnit.getTimeUnit())
                    .schedule(), builder.getStopEvent());
        }

        if (builder.getRepeat() > 0) {
            return AbstractTaskerTask.of(builder.getTaskId(), scheduler.buildTask(owner, runnable)
                    .delay(timeUnit.getTime((int) builder.getDelay()), timeUnit.getTimeUnit())
                    .repeat(timeUnit.getTime((int) builder.getRepeat()), timeUnit.getTimeUnit())
                    .schedule(), builder.getStopEvent());
        }

        throw new UnsupportedOperationException("Unsupported Tasker state!");
    }

    @Override
    public TaskState getState(TaskerTask taskerTask) {
        final ScheduledTask task = taskerTask.getTaskObject();

        switch (task.status()) {
            case CANCELLED:
                return TaskState.CANCELLED;
            case FINISHED:
                return TaskState.FINISHED;
            default:
                return TaskState.SCHEDULED;
        }
    }

    @Override
    public void cancel(TaskerTask task) {
        final ScheduledTask toCancel = task.getTaskObject();
        toCancel.cancel();
    }
}
