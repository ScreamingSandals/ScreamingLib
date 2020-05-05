package org.screamingsandals.lib.gamecore.core.data.file;

import org.screamingsandals.lib.gamecore.core.data.DataSource;
import org.screamingsandals.lib.gamecore.core.data.JsonUtils;
import org.screamingsandals.lib.debug.Debug;

import java.io.*;

public class JsonDataSource<T> extends DataSource<T> {
    private final File file;

    public JsonDataSource(File file, Class<T> tClass) {
        super(tClass);

        this.file = file;
    }

    @Override
    public T load() {
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
        if (checkFile(file)) {
            try (FileWriter writer = new FileWriter(file)) {
                JsonUtils.serializePretty(serializable, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean delete() {
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}
