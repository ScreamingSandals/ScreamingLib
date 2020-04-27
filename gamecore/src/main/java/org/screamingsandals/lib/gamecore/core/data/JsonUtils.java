package org.screamingsandals.lib.gamecore.core.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.io.Writer;

public class JsonUtils {
    public static Gson getPrettyGson() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create();
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .serializeNulls()
                .create();
    }

    public static <T> T deserialize(Reader reader, Class<T> type) {
        return getGson().fromJson(reader, type);
    }

    public static void serializePretty(Object instance, Writer writer) {
        getPrettyGson().toJson(instance, writer);
    }
}
