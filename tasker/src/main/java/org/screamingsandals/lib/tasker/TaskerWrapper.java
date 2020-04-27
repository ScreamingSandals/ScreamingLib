package org.screamingsandals.lib.tasker;

import lombok.Data;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.screamingsandals.lib.debug.Debug;

import java.util.concurrent.TimeUnit;

@Data
public class TaskerWrapper {
    private static Tasker instance;

    public static Tasker getInstance() {
        return instance;
    }

    @Data
    public static class SpigotTasker implements Tasker {
        private final Plugin plugin;

        public SpigotTasker(Object plugin) {
            this.plugin = (Plugin) plugin;

            TaskerWrapper.instance = this;
        }

        @Override
        public boolean hasStopped(BaseTask baseTask) {
            BukkitTask task = (BukkitTask) getRunningTasks().get(baseTask);
            if (task == null) {
                return true;
            }

            return task.isCancelled();
        }

        @Override
        public BaseTask runTask(BaseTask baseTask) {
            try {
                getRunningTasks()
                        .put(baseTask, plugin.getServer().getScheduler().runTask(plugin, baseTask));
            } catch (Exception ignored) {
                Debug.warn("You used Bukkit task on Bungee server, what the heck?!");
            }
            return baseTask;
        }

        @Override
        public BaseTask runTaskAsync(BaseTask baseTask) {
            try {
                getRunningTasks()
                        .put(baseTask, plugin.getServer().getScheduler().runTaskAsynchronously(plugin, baseTask));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return baseTask;
        }

        @Override
        public BaseTask runTaskLater(BaseTask baseTask, long delay, TimeUnit timeUnit) {
            try {
                getRunningTasks()
                        .put(baseTask, plugin.getServer().getScheduler().runTaskLater(plugin, baseTask, getBukkitTime(delay, timeUnit)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return baseTask;
        }

        @Override
        public BaseTask runTaskRepeater(BaseTask baseTask, long delay, long period, TimeUnit timeUnit) {
            try {
                getRunningTasks()
                        .put(baseTask, plugin.getServer().getScheduler().runTaskTimer(plugin, baseTask, delay, getBukkitTime(period, timeUnit)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return baseTask;
        }
    }

    @Data
    public static class BungeeTasker implements Tasker {
        private final net.md_5.bungee.api.plugin.Plugin plugin;

        public BungeeTasker(Object plugin) {
            this.plugin = (net.md_5.bungee.api.plugin.Plugin) plugin;

            TaskerWrapper.instance = this;
        }

        @Override
        public boolean hasStopped(BaseTask baseTask) {
            return getRunningTasks().get(baseTask) == null;

            //Sadly, bungee does not have any way to check if the task already stopped
        }

        @Override
        public BaseTask runTask(BaseTask baseTas) {
            Debug.warn("You used Bukkit task on Bungee server, what the heck?!");
            return null;
        }

        @Override
        public BaseTask runTaskAsync(BaseTask baseTask) {
            try {
                getRunningTasks().put(baseTask, plugin.getProxy()
                        .getScheduler().runAsync(plugin, baseTask));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return baseTask;
        }

        @Override
        public BaseTask runTaskLater(BaseTask baseTask, long delay, TimeUnit timeUnit) {
            try {
                getRunningTasks().put(baseTask, plugin.getProxy()
                        .getScheduler().schedule(plugin, baseTask, delay, timeUnit));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return baseTask;
        }

        @Override
        public BaseTask runTaskRepeater(BaseTask baseTask, long delay, long period, TimeUnit timeUnit) {
            try {
                getRunningTasks().put(baseTask, plugin.getProxy()
                        .getScheduler().schedule(
                                plugin, baseTask, delay, period, timeUnit));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return baseTask;
        }
    }

}
