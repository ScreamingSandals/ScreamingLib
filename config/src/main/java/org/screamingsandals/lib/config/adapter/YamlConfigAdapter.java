package org.screamingsandals.lib.config.adapter;

import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.screamingsandals.lib.config.exception.SConfigException;

import java.io.File;
import java.nio.file.Path;

public class YamlConfigAdapter extends BaseConfigAdapter {

    public YamlConfigAdapter(File file) throws SConfigException {
        this(file.toPath());
    }

    public YamlConfigAdapter(Path path) throws SConfigException {
        super(path, YAMLConfigurationLoader.builder()
                .setPath(path)
                .build());
    }
}
