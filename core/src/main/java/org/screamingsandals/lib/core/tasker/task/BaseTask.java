package org.screamingsandals.lib.core.tasker.task;

import org.screamingsandals.lib.core.tasker.TaskerUnit;
import org.screamingsandals.lib.core.tasker.type.AbstractTasker;

public abstract class BaseTask implements Runnable {

    public static BaseTask get(Runnable runnable) {
        return new BaseTask() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

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
        AbstractTasker.getInstance().destroyTask(this);
    }

    public boolean hasStopped() {
        return AbstractTasker.getInstance().hasStopped(this);
    }

    /**
     * Runs the task
     *
     * @return BaseTask instance
     */
    public BaseTask runTask() {
        return AbstractTasker.getInstance().runTask(this);
    }

    /**
     * Runs the task asynchronously
     *
     * @return BaseTask instance
     */
    public BaseTask runTaskAsync() {
        return AbstractTasker.getInstance().runTaskAsync(this);
    }

    /**
     * Runs the task later
     *
     * @param delay      - delay value
     * @param taskerUnit time unit for the delay value
     * @return BaseTask instance
     */
    public BaseTask runTaskLater(int delay, TaskerUnit taskerUnit) {
        return AbstractTasker.getInstance().runTaskLater(this, delay, taskerUnit);
    }

    /**
     * Runs the task repeatedly
     *
     * @param delay      - delay value
     * @param period     - repeating period value
     * @param taskerUnit time unit for the values
     * @return BaseTask instance
     */
    public BaseTask runTaskRepeater(int delay, int period, TaskerUnit taskerUnit) {
        return AbstractTasker.getInstance().runTaskRepeater(this, delay, period, taskerUnit);
    }
}