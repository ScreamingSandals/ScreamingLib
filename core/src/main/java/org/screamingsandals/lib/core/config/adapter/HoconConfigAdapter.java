package org.screamingsandals.lib.core.config.adapter;

import org.screamingsandals.lib.core.config.exception.SConfigException;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.File;
import java.nio.file.Path;

public class HoconConfigAdapter extends BaseConfigAdapter {

    public HoconConfigAdapter(File file) throws SConfigException {
        this(file.toPath());
    }

    public HoconConfigAdapter(Path path) throws SConfigException {
        super(path, HoconConfigurationLoader.builder()
                .path(path)
                .build());
    }

    public HoconConfigAdapter(File file, ConfigurationOptions options) throws SConfigException {
        this(file.toPath(), options);
    }

    public HoconConfigAdapter(Path path, ConfigurationOptions options) throws SConfigException {
        super(path, HoconConfigurationLoader.builder()
                .path(path)
                .defaultOptions(options)
                .build());
    }
}
