package org.screamingsandals.lib.core.tasker.type;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.Getter;
import org.screamingsandals.lib.core.reflect.SReflect;
import org.screamingsandals.lib.core.tasker.Tasker;
import org.screamingsandals.lib.core.tasker.task.BaseTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractTasker implements Tasker {
    protected final Logger log = LoggerFactory.getLogger(Tasker.class);
    private final Map<BaseTask, Object> runningTasks = new ConcurrentHashMap<>();

    /**
     * This is here for the {@link BaseTask#runTask()} and so on...
     */
    @Getter(AccessLevel.PUBLIC)
    private static AbstractTasker instance;

    public AbstractTasker() {
        instance = this;
    }

    @Override
    public Map<BaseTask, Object> getRunningTasks() {
        return ImmutableMap.copyOf(runningTasks);
    }

    @Override
    public void destroy() {
        getRunningTasks().keySet().forEach(baseTask -> {
            if (baseTask == null) {
                return;
            }

            try {
                var task = runningTasks.get(baseTask);
                SReflect.fastInvoke(task, "cancel");
            } catch (Exception ignored) {
            }
        });

        runningTasks.clear();
    }

    @Override
    public void destroyTask(BaseTask baseTask)  {
        if (baseTask == null) {
            return;
        }

        try {
            var task = runningTasks.get(baseTask);
            SReflect.fastInvoke(task, "cancel");
        } catch (Exception ignored) {
        }

        runningTasks.remove(baseTask);
    }

}
