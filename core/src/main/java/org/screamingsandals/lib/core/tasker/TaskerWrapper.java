package org.screamingsandals.lib.core.tasker;

import lombok.Data;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class TaskerWrapper {
    private static Logger log = LoggerFactory.getLogger(Tasker.class);
    private static Tasker instance;

    public static Tasker getInstance() {
        return instance;
    }

    @Data
    public static class BukkitTasker implements Tasker {
        private final Plugin plugin;

        public BukkitTasker(Object plugin) {
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
                getRunningTasks().put(baseTask, plugin.getServer().getScheduler()
                        .runTask(plugin, baseTask));
            } catch (Exception ignored) {
                log.warn("You used Bukkit task on Bungee server, what the heck?!");
            }
            return baseTask;
        }

        @Override
        public BaseTask runTaskAsync(BaseTask baseTask) {
            try {
                getRunningTasks().put(baseTask, plugin.getServer().getScheduler()
                        .runTaskAsynchronously(plugin, baseTask));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return baseTask;
        }

        @Override
        public BaseTask runTaskLater(BaseTask baseTask, int delay, TaskerTime taskerTime) {
            try {
                getRunningTasks().put(baseTask, plugin.getServer().getScheduler()
                        .runTaskLater(plugin, baseTask, TaskerTime.getBukkitValue(delay, taskerTime)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return baseTask;
        }

        @Override
        public BaseTask runTaskRepeater(BaseTask baseTask, int delay, int period, TaskerTime taskerTime) {
            try {
                getRunningTasks()
                        .put(baseTask, plugin.getServer().getScheduler()
                                .runTaskTimer(plugin, baseTask, delay, TaskerTime.getBukkitValue(period, taskerTime)));
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
            log.warn("You used Bukkit task on Bungee server, what the heck?!");
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
        public BaseTask runTaskLater(BaseTask baseTask, int delay, TaskerTime taskerTime) {
            try {
                if (taskerTime == TaskerTime.TICKS) {
                    getRunningTasks().put(baseTask, plugin.getProxy()
                            .getScheduler().schedule(plugin, baseTask, TaskerTime.getTimeUnitValue(delay, taskerTime), taskerTime.getTimeUnit()));
                } else {
                    getRunningTasks().put(baseTask, plugin.getProxy()
                            .getScheduler().schedule(plugin, baseTask, delay, taskerTime.getTimeUnit()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return baseTask;
        }

        @Override
        public BaseTask runTaskRepeater(BaseTask baseTask, int delay, int period, TaskerTime taskerTime) {
            try {
                if (taskerTime == TaskerTime.TICKS) {
                    getRunningTasks().put(baseTask, plugin.getProxy()
                            .getScheduler().schedule(
                                    plugin, baseTask, TaskerTime.getTimeUnitValue(delay, taskerTime), TaskerTime.getTimeUnitValue(period, taskerTime), taskerTime.getTimeUnit()));
                } else {
                    getRunningTasks().put(baseTask, plugin.getProxy()
                            .getScheduler().schedule(plugin, baseTask, delay, period, taskerTime.getTimeUnit()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return baseTask;
        }
    }

}
