package org.screamingsandals.lib.tasker.task;

public interface TaskerTask {

    Integer getId();

    /**
     * Returns the current state of this task.
     *
     * @return the current state of this task
     */
    TaskState getState();

    /**
     * Cancels this task.
     */
    void cancel();

    <P> P getTaskObject();
}
