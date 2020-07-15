package org.screamingsandals.lib.config;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.io.InputStream;

/* Because Toml class is not editable, so we work with map instead of Toml class */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class TomlConfigAdapter extends HashMapConfigAdapter {
    private File configFile;
    private InputStream inputStream;
    private TomlWriter tomlWriter = new TomlWriter();

    public TomlConfigAdapter(File configFile) {
        this.configFile = configFile;
    }

    public TomlConfigAdapter(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public static TomlConfigAdapter create(File configFile) {
        return new TomlConfigAdapter(configFile) {
        };
    }

    public static TomlConfigAdapter create(InputStream inputStream) {
        return new TomlConfigAdapter(inputStream) {
        };
    }

    @Override
    public void load() {
        if (inputStream != null) {
            setConfiguration(new Toml().read(inputStream).toMap());
            return;
        }

        try {
            setConfiguration(new Toml().read(configFile).toMap());
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
            tomlWriter.write(getConfiguration(), configFile);
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
