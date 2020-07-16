package org.screamingsandals.lib.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class GsonConfigAdapter extends HashMapConfigAdapter {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
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
            var map = gson.fromJson(new InputStreamReader(inputStream), Map.class);
            setConfiguration(map == null ? new HashMap<>() : map);
            return;
        }

        try {
            var map = gson.fromJson(new FileReader(configFile), Map.class);
            setConfiguration(map == null ? new HashMap<>() : map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        if (configFile == null) {
            return;
        }

        try (FileWriter writer = new FileWriter(configFile)) {
            var json = gson.toJson(getConfiguration());
            writer.append(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        load();
    }
}
