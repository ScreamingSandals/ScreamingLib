/*
 * Copyright 2023 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.impl.bukkit.tasker;

import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.block.BlockPlacement;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.impl.bukkit.tasker.task.FoliaTask;
import org.screamingsandals.lib.tasker.DefaultThreads;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.tasker.ThreadProperty;
import org.screamingsandals.lib.tasker.task.Task;
import org.screamingsandals.lib.tasker.task.TaskBase;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.Location;
import org.screamingsandals.lib.world.chunk.Chunk;

import java.util.Objects;
import java.util.function.Consumer;

@Service
public class FoliaTasker extends AbstractBukkitTasker {
    private final @NotNull Plugin plugin;
    private final @NotNull AsyncScheduler asyncScheduler;
    private final @NotNull RegionScheduler regionScheduler;
    private final @NotNull GlobalRegionScheduler globalRegionScheduler;

    public FoliaTasker(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.asyncScheduler = plugin.getServer().getAsyncScheduler();
        this.regionScheduler = plugin.getServer().getRegionScheduler();
        this.globalRegionScheduler = plugin.getServer().getGlobalRegionScheduler();
    }

    @Override
    protected @NotNull Task run0(@NotNull ThreadProperty property, @NotNull Runnable runnable) {
        if (property == DefaultThreads.ASYNC_THREAD) {
            return new FoliaTask(asyncScheduler.runNow(plugin, st -> runnable.run()));
        } else if (property instanceof Entity) {
            return new FoliaTask(Objects.requireNonNull(((Entity) property).as(org.bukkit.entity.Entity.class).getScheduler().run(plugin, st -> runnable.run(), null)));
        } else if (property instanceof BlockPlacement || property instanceof Location || property instanceof Chunk) {
            @NotNull Chunk chunk;
            if (property instanceof BlockPlacement) {
                chunk = ((BlockPlacement) property).location().getChunk();
            } else if (property instanceof Location) {
                chunk = ((Location) property).getChunk();
            } else {
                chunk = (Chunk) property;
            }
            return new FoliaTask(regionScheduler.run(plugin, chunk.getWorld().as(org.bukkit.World.class), chunk.getX(), chunk.getZ(), st -> runnable.run()));
        } else {
            return new FoliaTask(globalRegionScheduler.run(plugin, st -> runnable.run()));
        }
    }

    @Override
    protected @NotNull Task runDelayed0(@NotNull ThreadProperty property, @NotNull Runnable runnable, long delay, @NotNull TaskerTime delayUnit) {
        if (property == DefaultThreads.ASYNC_THREAD) {
            return new FoliaTask(asyncScheduler.runDelayed(plugin, st -> runnable.run(), delayUnit.getTime(delay), delayUnit.getTimeUnit()));
        } else if (property instanceof Entity) {
            return new FoliaTask(Objects.requireNonNull(((Entity) property).as(org.bukkit.entity.Entity.class).getScheduler().runDelayed(plugin, st -> runnable.run(), null, delayUnit.getBukkitTime(delay))));
        } else if (property instanceof BlockPlacement || property instanceof Location || property instanceof Chunk) {
            @NotNull Chunk chunk;
            if (property instanceof BlockPlacement) {
                chunk = ((BlockPlacement) property).location().getChunk();
            } else if (property instanceof Location) {
                chunk = ((Location) property).getChunk();
            } else {
                chunk = (Chunk) property;
            }
            return new FoliaTask(regionScheduler.runDelayed(plugin, chunk.getWorld().as(org.bukkit.World.class), chunk.getX(), chunk.getZ(), st -> runnable.run(), delayUnit.getBukkitTime(delay)));
        } else {
            return new FoliaTask(globalRegionScheduler.runDelayed(plugin, st -> runnable.run(), delayUnit.getBukkitTime(delay)));
        }
    }

    @Override
    protected @NotNull Task runRepeatedly0(@NotNull ThreadProperty property, @NotNull Runnable runnable, long period, @NotNull TaskerTime periodUnit) {
        return runDelayedAndRepeatedly0(property, runnable, 0L, TaskerTime.TICKS, period, periodUnit);
    }

    @Override
    protected @NotNull Task runRepeatedly0(@NotNull ThreadProperty property, @NotNull Consumer<@NotNull TaskBase> selfCancellable, long period, @NotNull TaskerTime periodUnit) {
        return runDelayedAndRepeatedly0(property, selfCancellable, 0L, TaskerTime.TICKS, period, periodUnit);
    }

    @Override
    protected @NotNull Task runDelayedAndRepeatedly0(@NotNull ThreadProperty property, @NotNull Runnable runnable, long delay, @NotNull TaskerTime delayUnit, long period, @NotNull TaskerTime periodUnit) {
        if (delay == 0) {
            // Folia does not like 0 delay
            delay = 1;
            delayUnit = TaskerTime.TICKS;
        }

        if (property == DefaultThreads.ASYNC_THREAD) {
            return new FoliaTask(asyncScheduler.runAtFixedRate(plugin, st -> runnable.run(), periodUnit.getTimeUnit().convert(delayUnit.getTime(delay), delayUnit.getTimeUnit()), periodUnit.getTime(period), periodUnit.getTimeUnit()));
        } else if (property instanceof Entity) {
            return new FoliaTask(Objects.requireNonNull(((Entity) property).as(org.bukkit.entity.Entity.class).getScheduler().runAtFixedRate(plugin, st -> runnable.run(), null, delayUnit.getBukkitTime(delay), periodUnit.getBukkitTime(period))));
        } else if (property instanceof BlockPlacement || property instanceof Location || property instanceof Chunk) {
            @NotNull Chunk chunk;
            if (property instanceof BlockPlacement) {
                chunk = ((BlockPlacement) property).location().getChunk();
            } else if (property instanceof Location) {
                chunk = ((Location) property).getChunk();
            } else {
                chunk = (Chunk) property;
            }
            return new FoliaTask(regionScheduler.runAtFixedRate(plugin, chunk.getWorld().as(org.bukkit.World.class), chunk.getX(), chunk.getZ(), st -> runnable.run(), delayUnit.getBukkitTime(delay), periodUnit.getBukkitTime(period)));
        } else {
            return new FoliaTask(globalRegionScheduler.runAtFixedRate(plugin, st -> runnable.run(), delayUnit.getBukkitTime(delay), periodUnit.getBukkitTime(period)));
        }
    }

    @Override
    protected @NotNull Task runDelayedAndRepeatedly0(@NotNull ThreadProperty property, @NotNull Consumer<@NotNull TaskBase> selfCancellable, long delay, @NotNull TaskerTime delayUnit, long period, @NotNull TaskerTime periodUnit) {
        if (delay == 0) {
            // Folia does not like 0 delay
            delay = 1;
            delayUnit = TaskerTime.TICKS;
        }

        if (property == DefaultThreads.ASYNC_THREAD) {
            return new FoliaTask(asyncScheduler.runAtFixedRate(plugin, st -> selfCancellable.accept(st::cancel), periodUnit.getTimeUnit().convert(delayUnit.getTime(delay), delayUnit.getTimeUnit()), periodUnit.getTime(period), periodUnit.getTimeUnit()));
        } else if (property instanceof Entity) {
            return new FoliaTask(Objects.requireNonNull(((Entity) property).as(org.bukkit.entity.Entity.class).getScheduler().runAtFixedRate(plugin, st -> selfCancellable.accept(st::cancel), null, delayUnit.getBukkitTime(delay), periodUnit.getBukkitTime(period))));
        } else if (property instanceof BlockPlacement || property instanceof Location || property instanceof Chunk) {
            @NotNull Chunk chunk;
            if (property instanceof BlockPlacement) {
                chunk = ((BlockPlacement) property).location().getChunk();
            } else if (property instanceof Location) {
                chunk = ((Location) property).getChunk();
            } else {
                chunk = (Chunk) property;
            }
            return new FoliaTask(regionScheduler.runAtFixedRate(plugin, chunk.getWorld().as(org.bukkit.World.class), chunk.getX(), chunk.getZ(), st -> selfCancellable.accept(st::cancel), delayUnit.getBukkitTime(delay), periodUnit.getBukkitTime(period)));
        } else {
            return new FoliaTask(globalRegionScheduler.runAtFixedRate(plugin, st -> selfCancellable.accept(st::cancel), delayUnit.getBukkitTime(delay), periodUnit.getBukkitTime(period)));
        }
    }
}
