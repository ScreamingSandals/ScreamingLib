package org.screamingsandals.lib.core.config.adapter;


import org.screamingsandals.lib.core.config.SConfig;
import org.screamingsandals.lib.core.config.exception.SConfigException;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.nio.file.Path;

public abstract class BaseConfigAdapter implements SConfig {
    protected final Path location;
    protected final ConfigurationLoader<?> loader;
    protected CommentedConfigurationNode root;

    public BaseConfigAdapter(File file, ConfigurationLoader<?> loader) throws SConfigException {
        this(file.toPath(), loader);
    }

    public BaseConfigAdapter(Path path, ConfigurationLoader<?> loader) throws SConfigException {
        this.location = path;
        this.loader = loader;

        try {
            root = (CommentedConfigurationNode) loader.load();
        } catch (Exception e) {
            throw new SConfigException("SConfig cannot be loaded!", e);
        }
    }

    @Override
    public CommentedConfigurationNode root() {
        return root;
    }

    @Override
    public CommentedConfigurationNode node(String key) {
        return root.node(key.split("\\."));
    }

    @Override
    public Path getPath() {
        return location;
    }

    @Override
    public void save() throws SConfigException {
        try {
            loader.save(root);
        } catch (ConfigurateException e) {
            throw new SConfigException("Saving failed!", e);
        }
    }

    @Override
    public void load() throws SConfigException {
        try {
            loader.load();
        } catch (ConfigurateException e) {
            throw new SConfigException("Loading failed!", e);
        }
    }
}
