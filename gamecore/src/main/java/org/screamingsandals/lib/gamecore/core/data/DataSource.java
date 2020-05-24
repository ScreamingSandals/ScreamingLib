package org.screamingsandals.lib.gamecore.core.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

@Data
@AllArgsConstructor
public abstract class DataSource<T> {
    private final Class<T> tClass;

    public T load() {
      return null;
    }

    public void save(Serializable serializable) {

    }

    public boolean delete() {
       return false;
    }

    public static boolean checkFile(File file) {
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
