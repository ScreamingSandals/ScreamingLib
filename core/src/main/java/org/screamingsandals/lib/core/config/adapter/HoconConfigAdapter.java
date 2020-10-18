package org.screamingsandals.lib.core.config.adapter;

import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.screamingsandals.lib.core.config.exception.SConfigException;

import java.io.File;
import java.nio.file.Path;

public class HoconConfigAdapter extends BaseConfigAdapter {

    public HoconConfigAdapter(File file) throws SConfigException {
        this(file.toPath());
    }

    public HoconConfigAdapter(Path path) throws SConfigException {
        super(path, HoconConfigurationLoader.builder()
                .setPath(path)
                .build());
    }
}
