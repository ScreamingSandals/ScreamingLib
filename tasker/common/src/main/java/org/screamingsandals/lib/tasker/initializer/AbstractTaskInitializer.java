package org.screamingsandals.lib.tasker.initializer;


import lombok.Getter;
import lombok.Setter;
import org.screamingsandals.lib.tasker.TaskBuilderImpl;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.annotations.AbstractMapping;

@AbstractMapping
public abstract class AbstractTaskInitializer {
    @Setter
    @Getter
    protected Tasker tasker;

    public abstract TaskerTask start(TaskBuilderImpl taskerBuilder);

    public abstract TaskState getState(TaskerTask taskerTask);
}
