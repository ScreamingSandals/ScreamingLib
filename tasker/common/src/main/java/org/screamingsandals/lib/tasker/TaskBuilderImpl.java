package org.screamingsandals.lib.tasker;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.tasker.initializer.AbstractTaskInitializer;
import org.screamingsandals.lib.tasker.task.TaskerTask;

@RequiredArgsConstructor
@Getter
public class TaskBuilderImpl implements Tasker.TaskBuilder {
    private final Runnable runnable;
    private final AbstractTaskInitializer initializer;
    private final Integer taskId;

    private boolean afterOneTick = false;
    private boolean async = false;
    private long delay;
    private long repeat;

    private TaskerTime timeUnit;

    @Override
    public Tasker.TaskBuilder afterOneTick() {
        if (async) {
            throw new UnsupportedOperationException("Cannot be delayed after one tick, the task is async!");
        }
        afterOneTick = true;
        return this;
    }

    @Override
    public Tasker.TaskBuilder async() {
        if (afterOneTick) {
            throw new UnsupportedOperationException("Cannot be async, the task is delayed after one tick!");
        }
        async = true;
        return this;
    }

    @Override
    public Tasker.TaskBuilder delay(long time, TaskerTime unit) {
        Preconditions.checkArgument(time < 0, "Time needs to be equals or bigger than 0!");
        delay = time;
        timeUnit = unit;
        return this;
    }

    @Override
    public Tasker.TaskBuilder repeat(long time, TaskerTime unit) {
        Preconditions.checkArgument(time < 0, "Time needs to be equals or bigger than 1!");
        repeat = time;
        timeUnit = unit;
        return this;
    }

    @Override
    public TaskerTask start() {
        Preconditions.checkNotNull(timeUnit, "Delay time unit is null!");
        final var task = Preconditions.checkNotNull(
                initializer.start(this),
                "Error occurred while trying to run task number " + taskId);
        initializer.getTasker().register(task);
        return task;
    }
}
