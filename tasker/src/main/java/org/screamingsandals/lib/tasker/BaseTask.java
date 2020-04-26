package org.screamingsandals.lib.tasker;

public abstract class BaseTask implements Runnable {

    @Override
    public void run() {

    }

    public void stop() {
        TaskerWrapper.getInstance().getTasker().destroyTask(this);
    }
}