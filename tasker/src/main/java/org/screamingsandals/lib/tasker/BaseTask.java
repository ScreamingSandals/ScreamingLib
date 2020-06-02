package org.screamingsandals.lib.tasker;

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
        TaskerWrapper.getInstance().destroyTask(this);
    }

    public boolean hasStopped() {
        return TaskerWrapper.getInstance().hasStopped(this);
    }

    /**
     * Runs the task
     *
     * @return BaseTask instance
     */
    public BaseTask runTask() {
        return TaskerWrapper.getInstance().runTask(this);
    }

    /**
     * Runs the task asynchronously
     *
     * @return BaseTask instance
     */
    public BaseTask runTaskAsync() {
        return TaskerWrapper.getInstance().runTaskAsync(this);
    }

    /**
     * Runs the task later
     *
     * @param delay      - delay value
     * @param taskerTime time unit for the delay value
     * @return BaseTask instance
     */
    public BaseTask runTaskLater(int delay, TaskerTime taskerTime) {
        return TaskerWrapper.getInstance().runTaskLater(this, delay, taskerTime);
    }

    /**
     * Runs the task repeatedly
     *
     * @param delay      - delay value
     * @param period     - repeating period value
     * @param taskerTime time unit for the values
     * @return BaseTask instance
     */
    public BaseTask runTaskRepeater(int delay, int period, TaskerTime taskerTime) {
        return TaskerWrapper.getInstance().runTaskRepeater(this, delay, period, taskerTime);
    }
}