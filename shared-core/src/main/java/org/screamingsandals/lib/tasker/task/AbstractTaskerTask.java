package org.screamingsandals.lib.tasker.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.tasker.Tasker;

import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
public abstract class AbstractTaskerTask implements TaskerTask {
    @Getter
    private final Integer id;
    @Getter
    private final Object taskObject;
    @Getter
    private final List<Consumer<TaskerTask>> taskEndHandlers;

    private TaskState state = TaskState.SCHEDULED;

    public static AbstractTaskerTask of(Integer id, Object taskObject, List<Consumer<TaskerTask>> taskEndHandlers) {
        final var task = new AbstractTaskerTask(id, taskObject, taskEndHandlers) {
        };
        Tasker.register(task);
        return task;
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
