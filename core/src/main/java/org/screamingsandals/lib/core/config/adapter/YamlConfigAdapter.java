package org.screamingsandals.lib.core.config.adapter;

import org.screamingsandals.lib.core.config.exception.SConfigException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.nio.file.Path;

public class YamlConfigAdapter extends BaseConfigAdapter {

    public YamlConfigAdapter(File file) throws SConfigException {
        this(file.toPath());
    }

    public YamlConfigAdapter(Path path) throws SConfigException {
        super(path, YamlConfigurationLoader.builder()
                .path(path)
                .build());
    }
}
