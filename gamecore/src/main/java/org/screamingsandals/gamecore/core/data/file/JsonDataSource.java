package org.screamingsandals.gamecore.core.data.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.screamingsandals.gamecore.core.data.DataSource;
import org.screamingsandals.lib.debug.Debug;

import java.io.*;

public class JsonDataSource<T> extends DataSource<T> {

    public JsonDataSource(File file, Class<T> tClass) {
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

    public static Gson getGson() {
        return new GsonBuilder()
                .serializeNulls()
                .create();
    }
}
