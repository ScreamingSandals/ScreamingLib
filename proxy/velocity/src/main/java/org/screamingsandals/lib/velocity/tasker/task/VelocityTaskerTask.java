package org.screamingsandals.lib.velocity.tasker.task;

import com.velocitypowered.api.scheduler.ScheduledTask;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.TaskerTask;

@Data
public class VelocityTaskerTask implements TaskerTask {
    private final @NotNull Integer id;
    private final @NotNull ScheduledTask taskObject;

    @Override
    public @NotNull TaskState getState() {
        switch (taskObject.status()) {
            case CANCELLED:
                return TaskState.CANCELLED;
            case FINISHED:
                return TaskState.FINISHED;
            default:
                return TaskState.SCHEDULED;
        }
    }

    @Override
    public void cancel() {
        try {
            taskObject.cancel();
        } catch (Exception e) {
            throw new UnsupportedOperationException("Exception while cancelling task " + id + "!", e);
        }

        Tasker.unregister(this);
    }
}
