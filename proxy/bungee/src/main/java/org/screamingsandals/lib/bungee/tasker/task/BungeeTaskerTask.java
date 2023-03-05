package org.screamingsandals.lib.bungee.tasker.task;

import lombok.Data;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.TaskerTask;

@Data
public class BungeeTaskerTask implements TaskerTask {
    private final @NotNull Integer id;
    private final @NotNull ScheduledTask taskObject;

    @Override
    public @NotNull TaskState getState() {
        //TODO: check task
        if (Tasker.getRunningTasks().containsKey(id)) {
            return TaskState.RUNNING;
        }
        return TaskState.FINISHED;
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
