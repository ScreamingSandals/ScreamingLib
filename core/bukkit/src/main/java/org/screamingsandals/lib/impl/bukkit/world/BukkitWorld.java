/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.world;

import org.bukkit.GameRule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.block.BlockPlacement;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.bukkit.block.BukkitBlockPlacement;
import org.screamingsandals.lib.impl.bukkit.particle.BukkitParticleConverter;
import org.screamingsandals.lib.impl.bukkit.particle.BukkitParticleConverter1_8;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.impl.nms.accessors.EnumParticleAccessor;
import org.screamingsandals.lib.impl.nms.accessors.ServerLevelAccessor;
import org.screamingsandals.lib.particle.Particle;
import org.screamingsandals.lib.spectator.audience.PlayerAudience;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.Location;
import org.screamingsandals.lib.world.World;
import org.screamingsandals.lib.world.chunk.Chunk;
import org.screamingsandals.lib.impl.world.chunk.Chunks;
import org.screamingsandals.lib.world.difficulty.DifficultyType;
import org.screamingsandals.lib.world.dimension.DimensionType;
import org.screamingsandals.lib.world.gamerule.GameRuleType;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class BukkitWorld extends BasicWrapper<org.bukkit.World> implements World {
    public BukkitWorld(@NotNull org.bukkit.World wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull UUID getUuid() {
        return wrappedObject.getUID();
    }

    @Override
    public @NotNull String getName() {
        return wrappedObject.getName();
    }

    @Override
    public int getMinY() {
        if (BukkitFeature.WORLD_MIN_HEIGHT.isSupported()) {
            return wrappedObject.getMinHeight();
        }
        return 0;
    }

    @Override
    public int getMaxY() {
        return wrappedObject.getMaxHeight();
    }

    @Override
    public @NotNull DifficultyType getDifficulty() {
        return DifficultyType.of(wrappedObject.getDifficulty());
    }

    @Override
    public @NotNull DimensionType getDimension() {
        return DimensionType.of(wrappedObject.getEnvironment());
    }

    @Override
    public @Nullable Chunk getChunkAt(int x, int z) {
        return Chunks.wrapChunk(wrappedObject.getChunkAt(x, z));
    }

    @Override
    public @Nullable Chunk getChunkAt(@NotNull Location location) {
        return Chunks.wrapChunk(wrappedObject.getChunkAt(location.as(org.bukkit.Location.class)));
    }

    @Override
    public @NotNull List<@NotNull Entity> getEntities() {
        return wrappedObject.getEntities().stream()
                .map(Entities::wrapEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @Nullable T getGameRuleValue(@NotNull GameRuleType holder) {
        if (BukkitFeature.GAME_RULE_API.isSupported()) {
            return (T) wrappedObject.getGameRuleValue(holder.as(GameRule.class));
        } else {
            var val = wrappedObject.getGameRuleValue(holder.platformName());
            if (val == null) {
                return null;
            }
            try {
                return (T) Integer.valueOf(val);
            } catch (Throwable ignored) {
                if ("true".equalsIgnoreCase(val) || "false".equalsIgnoreCase(val)) {
                    return (T) Boolean.valueOf(val);
                } else {
                    return (T) val;
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void setGameRuleValue(@NotNull GameRuleType holder, @NotNull T value) {
        if (BukkitFeature.GAME_RULE_API.isSupported()) {
            wrappedObject.setGameRule((GameRule<T>) holder.as(GameRule.class), value);
        } else {
            wrappedObject.setGameRuleValue(holder.platformName(), value.toString());
        }
    }

    @Override
    public long getTime() {
        return wrappedObject.getTime();
    }

    @Override
    public void setTime(long time) {
        wrappedObject.setTime(time);
    }

    @Override
    public void sendParticle(@NotNull Particle particle, @NotNull Location location) {
        if (!this.equals(location.getWorld())) {
            throw new IllegalArgumentException("The location of the sent particle is not in the correct world!");
        }

        if (BukkitFeature.EXTENDED_PARTICLE_METHOD.isSupported()) {
            // 1.13.1 +
            wrappedObject.spawnParticle(
                    particle.particleType().as(org.bukkit.Particle.class),
                    location.as(org.bukkit.Location.class),
                    particle.count(),
                    particle.offset().getX(),
                    particle.offset().getY(),
                    particle.offset().getZ(),
                    particle.particleData(),
                    particle.specialData() != null ? BukkitParticleConverter.convertParticleData(particle.specialData()) : null,
                    particle.longDistance()
            );
        } else if (BukkitFeature.PARTICLES_API.isSupported()) {
            // 1.9.+
            wrappedObject.spawnParticle(
                    particle.particleType().as(org.bukkit.Particle.class),
                    location.as(org.bukkit.Location.class),
                    particle.count(),
                    particle.offset().getX(),
                    particle.offset().getY(),
                    particle.offset().getZ(),
                    particle.particleData(),
                    particle.specialData() != null ? BukkitParticleConverter.convertParticleData(particle.specialData()) : null
                    // the official implementation set longDistance always to true for some reason in pre-flattening versions
            );
        } else {
            // 1.8.8
            var enumParticle = particle.particleType().as(EnumParticleAccessor.getType());
            Reflect.fastInvoke(
                    ClassStorage.getHandle(wrappedObject),
                    ServerLevelAccessor.getMethodA1(),
                    enumParticle,
                    particle.longDistance(),
                    (float) location.getX(),
                    (float) location.getY(),
                    (float) location.getZ(),
                    particle.count(),
                    (float) particle.offset().getX(),
                    (float) particle.offset().getY(),
                    (float) particle.offset().getZ(),
                    (float) particle.particleData(),
                    particle.specialData() != null ? BukkitParticleConverter1_8.convertParticleData(particle.specialData()) : new int[0]
            );
        }
    }

    @Override
    public boolean isSpawnKeptInMemory() {
        return wrappedObject.getKeepSpawnInMemory();
    }

    @Override
    public boolean isSpawningOfAnimalsAllowed() {
        return wrappedObject.getAllowAnimals();
    }

    @Override
    public boolean isSpawningOfMonstersAllowed() {
        return wrappedObject.getAllowMonsters();
    }

    @Override
    public @NotNull BlockPlacement getHighestBlockAt(int x, int z) {
        return new BukkitBlockPlacement(wrappedObject.getHighestBlockAt(x, z));
    }

    @Override
    public int getHighestYAt(int x, int z) {
        return wrappedObject.getHighestBlockYAt(x, z);
    }

    @Override
    public @NotNull Iterable<? extends @NotNull PlayerAudience> audiences() {
        return Server.getConnectedPlayersFromWorld(this);
    }
}
