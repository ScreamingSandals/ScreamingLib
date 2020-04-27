package org.screamingsandals.lib.tasker;

import java.util.concurrent.TimeUnit;

public abstract class BaseTask implements Runnable {

    /**
     * Runnable for the task
     */
    @Override
    public void run() {

    }

    /**
     * Cancels the task execution
     */
    public void stop() {
        TaskerWrapper.getInstance().destroyTask(this);
    }

    public boolean hasStopped() {
        return TaskerWrapper.getInstance().hasStopped(this);
    }

    /**
     * Runs the task
     * @return BaseTask instance
     */
    public BaseTask runTask() {
        return TaskerWrapper.getInstance().runTask(this);
    }

    /**
     * Runs the task asynchronously
     * @return BaseTask instance
     */
    public BaseTask runTaskAsync() {
        return TaskerWrapper.getInstance().runTaskAsync(this);
    }

    /**
     * Runs the task later
     * @param delay - delay value
     * @param timeUnit time unit for the delay value
     * @return BaseTask instance
     */
    public BaseTask runTaskLater(long delay, TimeUnit timeUnit) {
        return TaskerWrapper.getInstance().runTaskLater(this, delay, timeUnit);
    }

    /**
     * Runs the task repeatedly
     * @param delay - delay value
     * @param period - repeating period value
     * @param timeUnit time unit for the values
     * @return BaseTask instance
     */
    public BaseTask runTaskRepeater(long delay, long period, TimeUnit timeUnit) {
        return TaskerWrapper.getInstance().runTaskRepeater(this, delay, period, timeUnit);
    }
}