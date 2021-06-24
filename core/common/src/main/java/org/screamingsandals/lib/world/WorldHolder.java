package org.screamingsandals.lib.world;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.screamingsandals.lib.utils.Wrapper;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

public interface WorldHolder extends Wrapper, Serializable {

    UUID getUuid();

    String getName();

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
