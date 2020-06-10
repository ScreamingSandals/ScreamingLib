package org.screamingsandals.lib.gamecore.core.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.screamingsandals.lib.gamecore.core.data.DataSource.checkFile;

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

    public static <T> T deserialize(Reader reader, Type type) {
        return getGson().fromJson(reader, type);
    }

    public static void serializePretty(Serializable serializable, Writer writer) {
        getPrettyGson().toJson(serializable, writer);
    }

    public static void saveToFile(Serializable serializable, File file) {
        if (checkFile(file)) {
            try (var writer = Files.newBufferedWriter(Paths.get(file.getPath()), StandardCharsets.UTF_8)) {
                JsonUtils.serializePretty(serializable, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
