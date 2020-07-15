package org.screamingsandals.lib.config;

import com.google.gson.Gson;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.*;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class GsonConfigAdapter extends HashMapConfigAdapter {
    private Gson gson = new Gson();
    private File configFile;
    private InputStream inputStream;

    public GsonConfigAdapter(File configFile) {
        this.configFile = configFile;
    }

    public GsonConfigAdapter(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public static GsonConfigAdapter create(File configFile) {
        return new GsonConfigAdapter(configFile) {
        };
    }

    public static GsonConfigAdapter create(InputStream inputStream) {
        return new GsonConfigAdapter(inputStream) {
        };
    }

    @Override
    public void load() {
        if (inputStream != null) {
            setConfiguration(gson.fromJson(new InputStreamReader(inputStream), Map.class));
            return;
        }

        try {
            setConfiguration(gson.fromJson(new FileReader(configFile), Map.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        if (configFile == null) {
            return;
        }

        try {
            gson.toJson(getConfiguration(), new FileWriter(configFile));
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
