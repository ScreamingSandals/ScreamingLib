package org.screamingsandals.gamecore.core.data;

import lombok.Data;

import java.io.File;
import java.io.IOException;

@Data
public abstract class DataSource<T> {
    private Class<T> tClass;

    public DataSource(Class<T> tClass) {
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

    public boolean checkFile(File file) {
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
