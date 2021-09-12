package org.screamingsandals.lib.world;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.screamingsandals.lib.entity.EntityBasic;
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

public interface WorldHolder extends Wrapper, Serializable {

    UUID getUuid();

    String getName();

    int getMinY();

    int getMaxY();

    DifficultyHolder getDifficulty();

    DimensionHolder getDimension();

    Optional<ChunkHolder> getChunkAt(int x, int z);

    Optional<ChunkHolder> getChunkAt(LocationHolder location);

    List<EntityBasic> getEntities();

    default <T extends EntityBasic> List<T> getEntitiesByClass(Class<T> clazz) {
        return getEntities().stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .collect(Collectors.toList());
    }

    <T> T getGameRuleValue(GameRuleHolder holder);

    <T> void setGameRuleValue(GameRuleHolder holder, T value);

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
