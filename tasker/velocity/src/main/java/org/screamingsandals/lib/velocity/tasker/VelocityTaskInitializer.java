package org.screamingsandals.lib.velocity.tasker;

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
        ScheduledTask task = null;

        if (builder.isAfterOneTick() && builder.isAsync()) {
            throw new UnsupportedOperationException("Todo");
        }

        if (builder.isAfterOneTick()) {
            final var taskBuilder = scheduler.buildTask(owner, runnable);
            taskBuilder.delay(TaskerTime.TICKS.getTime(1), TaskerTime.TICKS.getTimeUnit());

            task = taskBuilder.schedule();
        }

        if (builder.isAsync()) {
            final var taskBuilder = scheduler.buildTask(owner, runnable);
            task = taskBuilder.schedule();
        }

        if (builder.getDelay() > 0) {
            final var taskBuilder = scheduler.buildTask(owner, runnable);
            taskBuilder.delay(builder.getTimeUnit().getTime((int) builder.getDelay()), builder.getTimeUnit().getTimeUnit());

            task = taskBuilder.schedule();
        }

        if (builder.getRepeat() > 0) {
            final var taskBuilder = scheduler.buildTask(owner, runnable);
            taskBuilder.delay(builder.getTimeUnit().getTime((int) builder.getDelay()),
                    builder.getTimeUnit().getTimeUnit());
            taskBuilder.repeat(builder.getTimeUnit().getTime((int) builder.getRepeat()),
                    builder.getTimeUnit().getTimeUnit());

            task = taskBuilder.schedule();
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
}
