package org.screamingsandals.lib.tasker.task;

import lombok.Getter;
import org.screamingsandals.lib.tasker.Tasker;

public abstract class AbstractTaskerTask implements TaskerTask {
    private final Tasker tasker;
    @Getter
    private final Integer id;
    @Getter
    private final Object taskObject;

    private TaskState state;

    public AbstractTaskerTask(Tasker tasker, Integer id, Object taskObject) {
        this.tasker = tasker;
        this.id = id;
        this.taskObject = taskObject;

        state = TaskState.SCHEDULED;
    }

    public TaskState getState() {
        if (state == TaskState.SCHEDULED) {
            return state;
        }

        if (state == TaskState.CANCELLED) {
            return state;
        }

        state = tasker.getState(this);
        return state;
    }

    @Override
    public void cancel() {
        tasker.cancel(this);
    }

}
