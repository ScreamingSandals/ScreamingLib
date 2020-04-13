package org.screamingsandals.gamecore.core.data;

import lombok.Data;

import java.io.File;
import java.io.IOException;

@Data
public abstract class DataSaver<T> {
    private final File file;
    private Class<T> tClass;

    public DataSaver(File file, Class<T> tClass) {
        this.file = file;
        this.tClass = tClass;
    }

    public T load() {
      return null;
    }

    public void save(Object object) {

    }

    public boolean delete() {
       return false;
    }

    public boolean checkFile() {
        if (!file.exists()) {
            try {
                return file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
