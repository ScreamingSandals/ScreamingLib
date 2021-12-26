package org.screamingsandals.lib.tasker.task;

public interface TaskerTask extends TaskBase {

    Integer getId();

    /**
     * Returns the current state of this task.
     *
     * @return the current state of this task
     */
    TaskState getState();

    <P> P getTaskObject();
}
