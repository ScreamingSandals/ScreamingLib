package org.screamingsandals.lib.world;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.particle.ParticleHolder;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.world.chunk.ChunkHolder;
import org.screamingsandals.lib.world.difficulty.DifficultyHolder;
import org.screamingsandals.lib.world.dimension.DimensionHolder;
import org.screamingsandals.lib.world.gamerule.GameRuleHolder;
import org.screamingsandals.lib.world.weather.WeatherHolder;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>An interface representing a world.</p>
 */
public interface WorldHolder extends Wrapper, Serializable {
    /**
     * <p>Gets the world's {@link UUID}.</p>
     *
     * @return the world uuid
     */
    UUID getUuid();

    /**
     * <p>Gets the world's name.</p>
     *
     * @return the world name
     */
    String getName();

    /**
     * <p>Gets the world's minimal Y coordinate.</p>
     *
     * @return the minimal Y coordinate
     */
    int getMinY();

    /**
     * <p>Gets the world's maximal Y coordinate.</p>
     *
     * @return the maximal Y coordinate
     */
    int getMaxY();

    /**
     * <p>Gets the world difficulty holder.</p>
     *
     * @return the world difficulty
     */
    DifficultyHolder getDifficulty();

    /**
     * <p>Gets the world dimension holder.</p>
     *
     * @return the world dimension
     */
    DimensionHolder getDimension();

    /**
     * <p>Gets the world's chunk at the supplied coordinates.</p>
     *
     * @param x the chunk X coordinate
     * @param z the chunk Y coordinate
     * @return the chunk holder, can be empty
     */
    Optional<ChunkHolder> getChunkAt(int x, int z);

    /**
     * <p>Gets the world's chunk at the supplied {@link LocationHolder}.</p>
     *
     * @param location the chunk location holder
     * @return the chunk holder, can be empty
     */
    Optional<ChunkHolder> getChunkAt(LocationHolder location);

    /**
     * <p>Gets the list of entities in this world.</p>
     *
     * @return the list of the entities in this world
     */
    List<EntityBasic> getEntities();

    /**
     * <p>Gets the list of entities extending the supplied class in this world.</p>
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
     * <p>Gets the value of the supplied gamerule holder in this world.</p>
     *
     * @param holder the gamerule holder
     * @param <T> the gamerule value type
     * @return the gamerule value
     */
    <T> T getGameRuleValue(GameRuleHolder holder);

    /**
     * <p>Sets the value of the supplied gamerule holder in this world.</p>
     *
     * @param holder the gamerule holder
     * @param value the gamerule value
     * @param <T> the gamerule value type
     */
    <T> void setGameRuleValue(GameRuleHolder holder, T value);

    /**
     * <p>Gets the time in this world.</p>
     *
     * @return the time in this world
     */
    long getTime();

    /**
     * <p>Sets the time in this world.</p>
     *
     * @param time the time
     */
    void setTime(long time);

    /**
     * <p>Spawns a particle at the specified location in this world.</p>
     *
     * @param particle the particle
     * @param location the location
     */
    void sendParticle(ParticleHolder particle, LocationHolder location);

    /**
     * <p>A gson {@link TypeAdapter} for serializing and deserializing a world holder.</p>
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
                final var toReturn = LocationMapper.getWorld(UUID.fromString(in.nextString())).orElseThrow();
                in.endObject();
                return toReturn;
            }
            throw new IOException("Name is not 'uuid'");
        }
    }
}
