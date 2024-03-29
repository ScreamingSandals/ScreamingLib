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

package org.screamingsandals.lib.world;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.api.types.server.WorldHolder;
import org.screamingsandals.lib.block.BlockPlacement;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.particle.Particle;
import org.screamingsandals.lib.spectator.audience.PlayerAudience;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.api.Wrapper;
import org.screamingsandals.lib.world.chunk.Chunk;
import org.screamingsandals.lib.world.difficulty.DifficultyType;
import org.screamingsandals.lib.world.dimension.DimensionType;
import org.screamingsandals.lib.world.gamerule.GameRuleType;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * An interface representing a world.
 */
public interface World extends Wrapper, RawValueHolder, PlayerAudience.ForwardingToMulti, WorldHolder {
    /**
     * Gets the world's {@link UUID}.
     *
     * @return the world uuid
     */
    @NotNull UUID getUuid();

    /**
     * Gets the world's name.
     *
     * @return the world name
     */
    @NotNull String getName();

    /**
     * Gets the world's minimal Y coordinate.
     *
     * @return the minimal Y coordinate
     */
    int getMinY();

    /**
     * Gets the world's maximal Y coordinate.
     *
     * @return the maximal Y coordinate
     */
    int getMaxY();

    /**
     * Gets the world difficulty holder.
     *
     * @return the world difficulty
     */
    @NotNull DifficultyType getDifficulty();

    /**
     * Gets the world dimension holder.
     *
     * @return the world dimension
     */
    @NotNull DimensionType getDimension();

    /**
     * Gets the world's chunk at the supplied coordinates.
     *
     * @param x the chunk X coordinate
     * @param z the chunk Y coordinate
     * @return the chunk holder, can be null
     */
    @Nullable Chunk getChunkAt(int x, int z);

    /**
     * Gets the world's chunk at the supplied {@link Location}.
     *
     * @param location the chunk location holder
     * @return the chunk holder, can be null
     */
    @Nullable Chunk getChunkAt(@NotNull Location location);

    /**
     * Gets the list of entities in this world.
     *
     * @return the list of the entities in this world
     */
    @NotNull List<@NotNull Entity> getEntities();

    /**
     * Gets the list of entities extending the supplied class in this world.
     *
     * @param clazz the entity type class
     * @param <T> the entity type
     * @return the list of entities
     */
    default <T extends Entity> @NotNull List<@NotNull T> getEntitiesByClass(Class<T> clazz) {
        return getEntities().stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .collect(Collectors.toList());
    }

    /**
     * Gets the value of the supplied gamerule holder in this world.
     *
     * @param holder the gamerule holder
     * @param <T> the gamerule value type
     * @return the gamerule value
     */
    <T> @Nullable T getGameRuleValue(@NotNull GameRuleType holder);

    /**
     * Sets the value of the supplied gamerule holder in this world.
     *
     * @param holder the gamerule holder
     * @param value the gamerule value
     * @param <T> the gamerule value type
     */
    <T> void setGameRuleValue(@NotNull GameRuleType holder, @NotNull T value);

    /**
     * Gets the time in this world.
     *
     * @return the time in this world
     */
    long getTime();

    /**
     * Sets the time in this world.
     *
     * @param time the time
     */
    void setTime(long time);

    /**
     * Spawns a particle at the specified location in this world.
     *
     * @param particle the particle
     * @param location the location
     */
    void sendParticle(@NotNull Particle particle, @NotNull Location location);

    /**
     * Determines if the spawn point of this world is loaded and being held in memory.
     *
     * @return is this world's spawn point loaded?
     */
    boolean isSpawnKeptInMemory();

    /**
     * Determines if spawning of animals is allowed in this world.
     *
     * @return is spawning of animals allowed in this world?
     */
    boolean isSpawningOfAnimalsAllowed();

    /**
     * Determines if spawning of monsters is allowed in this world.
     *
     * @return is spawning of monsters allowed in this world?
     */
    boolean isSpawningOfMonstersAllowed();

    /**
     * Gets the highest non-empty block at the given X and Z coordinates.
     *
     * @param x the x coordinate
     * @param z the z coordinate
     * @return the highest non-empty block
     */
    @NotNull BlockPlacement getHighestBlockAt(int x, int z);

    /**
     * Gets the highest non-empty Y coordinate at the given X and Z coordinates.
     *
     * @param x the x coordinate
     * @param z the z coordinate
     * @return the highest non-empty Y coordinate
     */
    int getHighestYAt(int x, int z);
}
