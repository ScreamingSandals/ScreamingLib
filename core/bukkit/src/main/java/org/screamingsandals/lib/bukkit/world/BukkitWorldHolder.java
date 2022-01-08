/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.lib.bukkit.world;

import net.kyori.adventure.audience.Audience;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.particle.BukkitParticleConverter;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.particle.ParticleHolder;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;
import org.screamingsandals.lib.world.WorldHolder;
import org.screamingsandals.lib.world.chunk.ChunkHolder;
import org.screamingsandals.lib.world.chunk.ChunkMapper;
import org.screamingsandals.lib.world.difficulty.DifficultyHolder;
import org.screamingsandals.lib.world.dimension.DimensionHolder;
import org.screamingsandals.lib.world.gamerule.GameRuleHolder;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ConfigSerializable
public class BukkitWorldHolder extends BasicWrapper<World> implements WorldHolder {
    public BukkitWorldHolder(World wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public UUID getUuid() {
        return wrappedObject.getUID();
    }

    @Override
    public String getName() {
        return wrappedObject.getName();
    }

    @Override
    public int getMinY() {
        if (Reflect.hasMethod(wrappedObject, "getMinHeight")) {
            return wrappedObject.getMinHeight();
        }
        return 0;
    }

    @Override
    public int getMaxY() {
        return wrappedObject.getMaxHeight();
    }

    @Override
    public DifficultyHolder getDifficulty() {
        return DifficultyHolder.of(wrappedObject.getDifficulty());
    }

    @Override
    public DimensionHolder getDimension() {
        return DimensionHolder.of(wrappedObject.getEnvironment());
    }

    @Override
    public Optional<ChunkHolder> getChunkAt(int x, int z) {
        return ChunkMapper.wrapChunk(wrappedObject.getChunkAt(x, z));
    }

    @Override
    public Optional<ChunkHolder> getChunkAt(LocationHolder location) {
        return ChunkMapper.wrapChunk(wrappedObject.getChunkAt(location.as(Location.class)));
    }

    @Override
    public List<EntityBasic> getEntities() {
        return wrappedObject.getEntities().stream()
                .map(EntityMapper::wrapEntity)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getGameRuleValue(GameRuleHolder holder) {
        if (Reflect.has("org.bukkit.GameRule")) {
            return (T) wrappedObject.getGameRuleValue(holder.as(GameRule.class));
        } else {
            var val = wrappedObject.getGameRuleValue(holder.platformName());
            if (val == null) {
                return null;
            }
            try {
                return (T) Integer.valueOf(val);
            } catch (Throwable ignored) {
                if (val.equalsIgnoreCase("true") || val.equalsIgnoreCase("false")) {
                    return (T) Boolean.valueOf(val);
                } else {
                    return (T) val;
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void setGameRuleValue(GameRuleHolder holder, T value) {
        if (Reflect.has("org.bukkit.GameRule")) {
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
    public void sendParticle(ParticleHolder particle, LocationHolder location) {
        wrappedObject.spawnParticle(
                particle.particleType().as(Particle.class),
                location.as(Location.class),
                particle.count(),
                particle.offset().getX(),
                particle.offset().getY(),
                particle.offset().getZ(),
                particle.particleData(),
                particle.specialData() != null ? BukkitParticleConverter.convertParticleData(particle.specialData()) : null,
                particle.longDistance()
        );
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
    public BlockHolder getHighestBlockAt(int x, int z) {
        return BlockMapper.wrapBlock(wrappedObject.getHighestBlockAt(x, z));
    }

    @Override
    public int getHighestYAt(int x, int z) {
        return wrappedObject.getHighestBlockYAt(x, z);
    }

    @Override
    @NotNull
    public Iterable<? extends Audience> audiences() {
        return Server.getConnectedPlayersFromWorld(this);
    }
}
