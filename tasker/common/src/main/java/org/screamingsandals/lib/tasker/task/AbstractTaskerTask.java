package org.screamingsandals.lib.tasker.task;

import lombok.Getter;
import org.screamingsandals.lib.tasker.Tasker;

public abstract class AbstractTaskerTask implements TaskerTask {
    @Getter
    private final Integer id;
    @Getter
    private final Object taskObject;

    private TaskState state;

    public AbstractTaskerTask(Integer id, Object taskObject) {
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

        state = Tasker.getState(this);
        return state;
    }

    @Override
    public void cancel() {
        Tasker.cancel(this);
    }

}
