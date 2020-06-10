package org.screamingsandals.lib.gamecore.core.data.file;

import org.screamingsandals.lib.gamecore.core.data.DataSource;
import org.screamingsandals.lib.gamecore.core.data.JsonUtils;
import org.screamingsandals.lib.debug.Debug;

import java.io.*;

public class JsonDataSource<T> extends DataSource<T> {
    private File file;
    private InputStream inputStream;

    public JsonDataSource(File file, Class<T> tClass) {
        super(tClass);
        this.file = file;
    }

    public JsonDataSource(InputStream inputStream, Class<T> tClass) {
        super(tClass);
        this.inputStream = inputStream;
    }

    @Override
    public T load() {
        if (file == null) {
            try (Reader reader = new InputStreamReader(inputStream)) {
                return JsonUtils.deserialize(reader, getTClass());
            } catch (IOException e) {
                Debug.warn("Some error occurred while parsing data!");
                e.printStackTrace();
            }
        }

        if (checkFile(file)) {
            try (Reader reader = new FileReader(file)) {
                return JsonUtils.deserialize(reader, getTClass());
            } catch (IOException e) {
                Debug.warn("Some error occurred while parsing data!");
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void save(Serializable serializable) {
        if (file == null) {
            return;
        }

        JsonUtils.saveToFile(serializable, file);
    }

    @Override
    public boolean delete() {
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}
