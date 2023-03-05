package org.screamingsandals.lib.bukkit.tasker.task;

import lombok.Data;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.TaskerTask;

@Data
public class BukkitTaskerTask implements TaskerTask {
    private final @NotNull Integer id;
    private final @NotNull BukkitTask taskObject;

    @Override
    public void cancel() {
        try {
            taskObject.cancel();
        } catch (Exception e) {
            throw new UnsupportedOperationException("Exception while cancelling task " + id + "!", e);
        }

        Tasker.unregister(this);
    }

    @Override
    public @NotNull TaskState getState() {
        if (taskObject.isCancelled()) {
            return TaskState.FINISHED;
        }

        return TaskState.RUNNING;
    }
}
