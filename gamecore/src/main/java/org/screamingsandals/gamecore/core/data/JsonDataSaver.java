package org.screamingsandals.gamecore.core.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.screamingsandals.lib.debug.Debug;

import java.io.*;

public class JsonDataSaver<T> extends DataSaver<T> {

    public JsonDataSaver(File file, Class<T> tClass) {
        super(file, tClass);
    }

    @Override
    public T load() {
        if (checkFile()) {
            try (Reader reader = new FileReader(getFile())) {
                return deserialize(reader, getTClass());
            } catch (IOException e) {
                Debug.warn("Some error occurred while parsing data!");
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void save(Object object) {
        if (checkFile()) {
            try (FileWriter writer = new FileWriter(getFile())) {
                serialize(object, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean delete() {
        if (getFile().exists()) {
            return getFile().delete();
        }
        return false;
    }

    private void serialize(Object instance, Writer writer) {
        new GsonBuilder().serializeNulls().setPrettyPrinting().create().toJson(instance, writer);
    }

    private <Y> Y deserialize(Reader reader, Class<Y> type) {
        return getGson().fromJson(reader, type);
    }

    private Gson getGson() {
        return new GsonBuilder()
                .serializeNulls()
                .create();
    }
}
