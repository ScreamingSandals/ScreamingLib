package org.screamingsandals.lib.core.config.adapter;

import org.screamingsandals.lib.core.config.exception.SConfigException;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;

import java.io.File;
import java.nio.file.Path;

public class GsonConfigAdapter extends BaseConfigAdapter {

    public GsonConfigAdapter(File file) throws SConfigException {
        this(file.toPath());
    }

    public GsonConfigAdapter(Path path) throws SConfigException {
        super(path, GsonConfigurationLoader.builder()
                .path(path)
                .build());
    }
}
