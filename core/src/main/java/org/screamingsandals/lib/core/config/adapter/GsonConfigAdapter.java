package org.screamingsandals.lib.config.adapter;

import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import org.screamingsandals.lib.config.exception.SConfigException;

import java.io.File;
import java.nio.file.Path;

public class GsonConfigAdapter extends BaseConfigAdapter {

    public GsonConfigAdapter(File file) throws SConfigException {
        this(file.toPath());
    }

    public GsonConfigAdapter(Path path) throws SConfigException {
        super(path, GsonConfigurationLoader.builder()
                .setPath(path)
                .build());
    }
}
