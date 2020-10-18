package org.screamingsandals.lib.core.config.adapter;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.screamingsandals.lib.core.config.SConfig;
import org.screamingsandals.lib.core.config.exception.SConfigException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public abstract class BaseConfigAdapter implements SConfig {
    protected final Path location;
    protected final ConfigurationLoader<?> loader;
    protected CommentedConfigurationNode node;

    public BaseConfigAdapter(File file, ConfigurationLoader<?> loader) throws SConfigException {
        this(file.toPath(), loader);
    }

    public BaseConfigAdapter(Path path, ConfigurationLoader<?> loader) throws SConfigException {
        this.location = path;
        this.loader = loader;

        try {
            node = (CommentedConfigurationNode) loader.load();
        } catch (Exception e) {
            throw new SConfigException("SConfig cannot be loaded!", e);
        }
    }

    @Override
    public CommentedConfigurationNode getRoot() {
        return node;
    }

    @Override
    public CommentedConfigurationNode get(String key) {
        return node.getNode(key);
    }

    @Override
    public Path getPath() {
        return location;
    }

    @Override
    public void save() throws SConfigException {
        try {
            loader.save(node);
        } catch (IOException e) {
            throw new SConfigException("Saving failed!", e);
        }
    }

    @Override
    public void load() throws SConfigException {
        try {
            loader.load();
        } catch (IOException e) {
            throw new SConfigException("Loading failed!", e);
        }
    }
}
