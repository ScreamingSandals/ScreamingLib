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

package org.screamingsandals.lib.world;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.particle.ParticleHolder;
import org.screamingsandals.lib.spectator.audience.PlayerAudience;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.world.chunk.ChunkHolder;
import org.screamingsandals.lib.world.difficulty.DifficultyHolder;
import org.screamingsandals.lib.world.dimension.DimensionHolder;
import org.screamingsandals.lib.world.gamerule.GameRuleHolder;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * An interface representing a world.
 */
public interface WorldHolder extends Wrapper, RawValueHolder, Serializable, PlayerAudience.ForwardingToMulti {
    /**
     * Gets the world's {@link UUID}.
     *
     * @return the world uuid
     */
    UUID getUuid();

    /**
     * Gets the world's name.
     *
     * @return the world name
     */
    String getName();

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
    DifficultyHolder getDifficulty();

    /**
     * Gets the world dimension holder.
     *
     * @return the world dimension
     */
    DimensionHolder getDimension();

    /**
     * Gets the world's chunk at the supplied coordinates.
     *
     * @param x the chunk X coordinate
     * @param z the chunk Y coordinate
     * @return the chunk holder, can be empty
     */
    Optional<ChunkHolder> getChunkAt(int x, int z);

    /**
     * Gets the world's chunk at the supplied {@link LocationHolder}.
     *
     * @param location the chunk location holder
     * @return the chunk holder, can be empty
     */
    Optional<ChunkHolder> getChunkAt(LocationHolder location);

    /**
     * Gets the list of entities in this world.
     *
     * @return the list of the entities in this world
     */
    List<EntityBasic> getEntities();

    /**
     * Gets the list of entities extending the supplied class in this world.
     *
     * @param clazz the entity type class
     * @param <T> the entity type
     * @return the list of entities
     */
    default <T extends EntityBasic> List<T> getEntitiesByClass(Class<T> clazz) {
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
    <T> T getGameRuleValue(GameRuleHolder holder);

    /**
     * Sets the value of the supplied gamerule holder in this world.
     *
     * @param holder the gamerule holder
     * @param value the gamerule value
     * @param <T> the gamerule value type
     */
    <T> void setGameRuleValue(GameRuleHolder holder, T value);

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
    void sendParticle(ParticleHolder particle, LocationHolder location);

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
    BlockHolder getHighestBlockAt(int x, int z);

    /**
     * Gets the highest non-empty Y coordinate at the given X and Z coordinates.
     *
     * @param x the x coordinate
     * @param z the z coordinate
     * @return the highest non-empty Y coordinate
     */
    int getHighestYAt(int x, int z);

    /**
     * A gson {@link TypeAdapter} for serializing and deserializing a world holder.
     */
    class WorldHolderTypeAdapter extends TypeAdapter<WorldHolder> {
        @Override
        public void write(JsonWriter out, WorldHolder value) throws IOException {
            out.beginObject();
            out.name("uuid");
            out.value(value.getUuid().toString());
            out.endObject();
        }

        @Override
        public WorldHolder read(JsonReader in) throws IOException {
            in.beginObject();
            final var name = in.nextName();
            if (name.equals("uuid")) {
                final var toReturn = WorldMapper.getWorld(UUID.fromString(in.nextString())).orElseThrow();
                in.endObject();
                return toReturn;
            }
            throw new IOException("Name is not 'uuid'");
        }
    }
}
