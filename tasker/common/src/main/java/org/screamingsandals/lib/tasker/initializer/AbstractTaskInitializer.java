package org.screamingsandals.lib.tasker.initializer;


import org.screamingsandals.lib.tasker.TaskBuilderImpl;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.annotations.AbstractService;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+)\\.initializer\\.Abstract(?<className>.+)$"
)
public abstract class AbstractTaskInitializer {

    public abstract TaskerTask start(TaskBuilderImpl taskerBuilder);

    public abstract TaskState getState(TaskerTask taskerTask);

    public abstract void cancel(TaskerTask task);
}
